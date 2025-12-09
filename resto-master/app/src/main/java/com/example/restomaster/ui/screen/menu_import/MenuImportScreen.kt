package com.example.restomaster.ui.screen.menu_import

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.exifinterface.media.ExifInterface
import com.example.restomaster.data.model.OcrTextLine
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuImportScreen(
    viewModel: MenuImportViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onImportFinished: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.onImageSelected(bitmap, rotationDegrees = 0)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            loadBitmapWithOrientation(context, uri)?.let { (bitmap, rotation) ->
                viewModel.onImageSelected(bitmap, rotationDegrees = rotation)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("メニュー写真インポート") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isProcessing) {
                CircularProgressIndicator()
                Spacer(Modifier.height(spacing.md))
                Text("処理中...")
            } else {
                when (state.step) {
                    MenuImportViewModel.Step.SelectImage -> {
                        SelectImageStep(
                            onCameraClick = { cameraLauncher.launch(null) },
                            onGalleryClick = { galleryLauncher.launch("image/*") }
                        )
                    }
                    MenuImportViewModel.Step.Interactive -> {
                        InteractiveStep(
                            originalImage = state.originalImage,
                            ocrLines = state.ocrLines,
                            onLineClick = viewModel::onLineClick,
                            onFinish = onImportFinished,
                            onRetake = viewModel::onReset
                        )
                        
                        state.selectedLineForAdd?.let { selectedLine ->
                            AddMenuItemDialog(
                                initialName = selectedLine.text,
                                onDismiss = viewModel::onDismissAddDialog,
                                onAdd = viewModel::onAddMenuItem
                            )
                        }
                    }
                    MenuImportViewModel.Step.Done -> {
                        DoneStep(
                            onFinish = onImportFinished,
                            onImportMore = viewModel::onReset
                        )
                    }
                }
            }

            if (state.errorMessage != null) {
                Spacer(Modifier.height(spacing.md))
                Text(
                    text = state.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun SelectImageStep(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "メニューの写真を撮影または選択してください",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(spacing.lg))
        
        DaPrimaryButton(text = "カメラで撮影", onClick = onCameraClick)
        Spacer(Modifier.height(spacing.md))
        DaPrimaryButton(text = "ギャラリーから選択", onClick = onGalleryClick)
    }
}

@Composable
private fun InteractiveStep(
    originalImage: Bitmap?,
    ocrLines: List<OcrTextLine>,
    onLineClick: (OcrTextLine) -> Unit,
    onFinish: () -> Unit,
    onRetake: () -> Unit
) {
    val spacing = LocalSpacing.current
    
    Column(Modifier.fillMaxSize()) {
        Text(
            "読み取った文字をタップしてメニューに追加",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = spacing.sm)
        )
        




        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(spacing.sm)
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            if (originalImage != null) {
                // Use a BoxWithConstraints to scale coordinates
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val imageBitmap = originalImage.asImageBitmap()
                    // Calculate scale to fit image in box while maintaining aspect ratio
                    val imageRatio = originalImage.width.toFloat() / originalImage.height.toFloat()
                    val boxRatio = maxWidth.value / maxHeight.value
                    
                    val scale = if (imageRatio > boxRatio) {
                        maxWidth.value / originalImage.width.toFloat()
                    } else {
                        maxHeight.value / originalImage.height.toFloat()
                    }
                    
                    val displayWidth = originalImage.width * scale
                    val displayHeight = originalImage.height * scale
                    
                    val offsetX = (maxWidth.value - displayWidth) / 2
                    val offsetY = (maxHeight.value - displayHeight) / 2

                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Menu Image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    
                    // Draw clickable overlays
                    ocrLines.forEach { line ->
                        line.boundingBox?.let { rect ->
                            val scaledLeft = (rect.left * scale) + offsetX
                            val scaledTop = (rect.top * scale) + offsetY
                            val scaledWidth = rect.width() * scale
                            val scaledHeight = rect.height() * scale
                            
                            Box(
                                modifier = Modifier
                                    .offset(scaledLeft.dp, scaledTop.dp)
                                    .size(scaledWidth.dp, scaledHeight.dp)
                                    .border(1.dp, Color.Green.copy(alpha = 0.5f))
                                    .clickable { onLineClick(line) }
                            )
                        }
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.material3.TextButton(onClick = onRetake) {
                Text("撮り直す")
            }
            DaPrimaryButton(text = "完了", onClick = onFinish)
        }
    }
}

@Composable
private fun AddMenuItemDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onAdd: (String, Int) -> Unit
) {
    var name by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(initialName) }
    var priceText by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("メニュー追加") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("商品名") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("価格") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            DaPrimaryButton(
                text = "追加",
                onClick = {
                    val price = priceText.toIntOrNull() ?: 0
                    onAdd(name, price)
                }
            )
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}

private fun loadBitmapWithOrientation(
    context: android.content.Context,
    uri: Uri
): Pair<Bitmap, Int>? {
    return try {
        val rotation = context.contentResolver.openInputStream(uri)?.use { input ->
            ExifInterface(input).rotationDegrees()
        } ?: 0

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        Pair(bitmap, rotation)
    } catch (e: Exception) {
        null
    }
}

private fun ExifInterface.rotationDegrees(): Int {
    return when (getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
}

@Composable
private fun DoneStep(
    onFinish: () -> Unit,
    onImportMore: () -> Unit
) {
    val spacing = LocalSpacing.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = "Success",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(spacing.md))
        Text(
            "インポートが完了しました！",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(spacing.lg))
        
        DaPrimaryButton(text = "メニュー一覧へ戻る", onClick = onFinish)
        Spacer(Modifier.height(spacing.md))
        androidx.compose.material3.TextButton(onClick = onImportMore) {
            Text("続けてインポートする")
        }
    }
}
