package com.example.restomaster.ui.screen.menu_import

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.data.model.MenuCandidate
import com.example.restomaster.data.model.OcrTextLine
import com.example.restomaster.data.ocr.MlKitTextRecognizer
import com.example.restomaster.domain.ocr.MenuOcrProcessor
import com.example.restomaster.domain.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuImportViewModel @Inject constructor(
    private val mlKitTextRecognizer: MlKitTextRecognizer,
    private val menuOcrProcessor: MenuOcrProcessor,
    private val menuRepository: MenuRepository
) : ViewModel() {

    enum class Step { SelectImage, Interactive, Done }

    data class ImportUiState(
        val step: Step = Step.SelectImage,
        val originalImage: Bitmap? = null,
        val ocrLines: List<OcrTextLine> = emptyList(),
        val selectedLineForAdd: OcrTextLine? = null,
        val isProcessing: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(ImportUiState())
    val uiState: StateFlow<ImportUiState> = _uiState.asStateFlow()

    fun onImageSelected(bitmap: Bitmap, rotationDegrees: Int = 0) {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isProcessing = true, 
                    originalImage = rotateForPreview(bitmap, rotationDegrees),
                    errorMessage = null
                ) 
            }
            
            try {
                val lines = mlKitTextRecognizer.recognize(bitmap, rotationDegrees)
                // Skip bulk processing, go straight to interactive mode with raw lines
                
                _uiState.update {
                    it.copy(
                        step = Step.Interactive,
                        ocrLines = lines,
                        isProcessing = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        errorMessage = "OCR processing failed: ${e.message}"
                    )
                }
            }
        }
    }

    fun onLineClick(line: OcrTextLine) {
        _uiState.update { it.copy(selectedLineForAdd = line) }
    }

    fun onDismissAddDialog() {
        _uiState.update { it.copy(selectedLineForAdd = null) }
    }

    fun onAddMenuItem(name: String, price: Int) {
        viewModelScope.launch {
            try {
                val item = MenuItemEntity(
                    storeId = 1L,
                    name = name,
                    price = price,
                    description = "Imported from photo",
                    category = "Imported",
                    isAvailable = true
                )
                menuRepository.addMenuItem(item)
                _uiState.update { it.copy(selectedLineForAdd = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to add item: ${e.message}") }
            }
        }
    }
    
    fun onReset() {
        _uiState.value = ImportUiState()
    }

    private fun rotateForPreview(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        if (rotationDegrees % 360 == 0) return bitmap
        val matrix = android.graphics.Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
