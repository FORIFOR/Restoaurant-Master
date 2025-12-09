package com.example.restomaster.ui.ocr

import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.domain.repository.MenuRepository
import com.example.restomaster.domain.usecase.MenuOcrGeminiUseCase
import com.example.restomaster.domain.usecase.MenuOcrUseCase
import com.example.restomaster.domain.usecase.ParsedMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrResultViewModel @Inject constructor(
    private val menuOcrUseCase: MenuOcrUseCase,
    private val menuOcrGeminiUseCase: MenuOcrGeminiUseCase,
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OcrResultUiState())
    val uiState: StateFlow<OcrResultUiState> = _uiState.asStateFlow()

    fun processImage(uri: android.net.Uri, context: android.content.Context) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val bitmap = if (android.os.Build.VERSION.SDK_INT < 28) {
                    android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = android.graphics.ImageDecoder.createSource(context.contentResolver, uri)
                    android.graphics.ImageDecoder.decodeBitmap(source)
                }
                
                val rotation = context.contentResolver.openInputStream(uri)?.use { input ->
                    ExifInterface(input).rotationDegrees()
                } ?: 0

                val rotated = rotateBitmap(bitmap, rotation)
                // Resize if too large to prevent OOM during ML processing
                val scaledBitmap = scaleBitmapDown(rotated, 1024)
                
                val geminiItems = menuOcrGeminiUseCase.execute(scaledBitmap)
                val items = if (geminiItems.isNotEmpty()) {
                    geminiItems
                } else {
                    android.util.Log.i("OCR_FLOW", "Gemini returned 0 items, fallback to MLKit")
                    menuOcrUseCase.execute(scaledBitmap)
                }
                _uiState.value = _uiState.value.copy(
                    parsedItems = items,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = originalWidth
        var resizedHeight = originalHeight

        if (originalHeight > originalWidth) {
            if (originalHeight > maxDimension) {
                resizedHeight = maxDimension
                resizedWidth = (originalWidth * (maxDimension.toFloat() / originalHeight)).toInt()
            }
        } else {
            if (originalWidth > maxDimension) {
                resizedWidth = maxDimension
                resizedHeight = (originalHeight * (maxDimension.toFloat() / originalWidth)).toInt()
            }
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, true)
    }

    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        if (rotationDegrees % 360 == 0) return bitmap
        val matrix = android.graphics.Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun ExifInterface.rotationDegrees(): Int {
        return when (getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    fun updateItem(index: Int, newItem: ParsedMenuItem) {
        val currentList = _uiState.value.parsedItems.toMutableList()
        currentList[index] = newItem
        _uiState.value = _uiState.value.copy(parsedItems = currentList)
    }

    fun deleteItem(index: Int) {
        val currentList = _uiState.value.parsedItems.toMutableList()
        currentList.removeAt(index)
        _uiState.value = _uiState.value.copy(parsedItems = currentList)
    }

    fun saveAll(category: String, imagePath: String?) {
        viewModelScope.launch {
            val entities = _uiState.value.parsedItems.map {
                MenuItemEntity(
                    storeId = 1L, // TODO: Get from settings
                    name = it.name,
                    price = it.price,
                    category = category,
                    description = null,
                    imagePath = imagePath
                )
            }
            entities.forEach { menuRepository.addMenuItem(it) }
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}

data class OcrResultUiState(
    val parsedItems: List<ParsedMenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)
