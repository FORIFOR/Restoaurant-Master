package com.example.restomaster.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.data.local.entity.MenuItemEntity
import com.example.restomaster.ui.components.DaCard
import com.example.restomaster.ui.theme.GrayTextSecondary
import com.example.restomaster.ui.theme.LocalSpacing
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuListScreen(
    viewModel: MenuListViewModel = hiltViewModel(),
    onNavigateCamera: () -> Unit,
    onNavigateImport: () -> Unit,
    onNavigateDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<MenuItemEntity?>(null) }
    var showTranslationDialog by remember { mutableStateOf<String?>(null) }
    val spacing = LocalSpacing.current
    val categories = listOf("Food", "Drink", "Dessert")

    val context = androidx.compose.ui.platform.LocalContext.current
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onNavigateCamera()
        } else {
            android.widget.Toast.makeText(context, "カメラの権限が必要です", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    // We need a way to pass the image path to the dialog.
    // Ideally, the ViewModel should hold this temporary state or we use a local state here.
    // Let's use a local state for simplicity, but we need to expose a setter to the launcher callback.
    // Wait, the launcher callback is inside the Composable, so it can access local state.
    var tempImagePath by remember { mutableStateOf<String?>(null) }

    // Update the launcher callback to set tempImagePath
    val pickMediaWithState = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val path = copyImageToAppStorage(context, uri)
            tempImagePath = path
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("メニュー管理") },
                actions = {
                    // Language Selector
                    Box {
                        var expanded by remember { mutableStateOf(false) }
                        val languages = listOf(
                            null to "日本語 (Original)",
                            "en" to "English",
                            "zh-Hans" to "简体中文",
                            "fr" to "Français",
                            "it" to "Italiano"
                        )
                        val currentLabel = languages.find { it.first == uiState.selectedLanguage }?.second ?: "言語選択"

                        TextButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text(currentLabel)
                        }

                        androidx.compose.material3.DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            languages.forEach { (code, label) ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        expanded = false
                                        if (code != null && code != uiState.selectedLanguage) {
                                            // Show confirmation dialog for translation
                                            showTranslationDialog = code
                                        } else {
                                            // Just switch to Japanese (no translation needed)
                                            viewModel.selectLanguage(code)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = {
                        val permission = android.Manifest.permission.CAMERA
                        if (androidx.core.content.ContextCompat.checkSelfPermission(context, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            onNavigateCamera()
                        } else {
                            permissionLauncher.launch(permission)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(bottom = spacing.md)
                ) {
                    Icon(Icons.Filled.PhotoCamera, contentDescription = "Scan Menu")
                }
                FloatingActionButton(
                    onClick = onNavigateImport,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.padding(bottom = spacing.md)
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Import from Photo")
                }
                FloatingActionButton(onClick = {
                    tempImagePath = null // Reset temp image
                    showAddDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "手動追加")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(uiState.selectedCategory),
                edgePadding = spacing.lg,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = uiState.selectedCategory == category,
                        onClick = { viewModel.selectCategory(category) },
                        text = { Text(category) }
                    )
                }
            }

            // Translation progress indicator
            if (uiState.translationInProgress) {
                val (current, total) = uiState.translationProgress
                val progressValue = if (total > 0) current.toFloat() / total.toFloat() else 0f
                
                androidx.compose.material3.LinearProgressIndicator(
                    progress = progressValue,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.lg)
                )
                Text(
                    text = "翻訳中... ${uiState.translationProgress.first} / ${uiState.translationProgress.second}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = spacing.lg, vertical = spacing.xs)
                )
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = spacing.lg, vertical = spacing.md)
            ) {
                items(uiState.displayItems) { displayItem ->
                    MenuItemRow(
                        displayItem = displayItem,
                        onClick = { onNavigateDetail(displayItem.originalItem.id) },
                        onDelete = { deleteTarget = displayItem.originalItem },
                        onToggleAvailability = { viewModel.toggleAvailability(displayItem.originalItem) }
                    )
                    Spacer(modifier = Modifier.height(spacing.sm))
                }
            }
        }

        if (showAddDialog) {
            AddMenuDialog(
                initialCategory = uiState.selectedCategory,
                onDismiss = { showAddDialog = false },
                onConfirm = { name, price, category, description, imageUrl ->
                    viewModel.addMenuItem(name, price, category, description, imageUrl)
                    showAddDialog = false
                },
                onSelectImage = {
                    pickMediaWithState.launch(androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                selectedImageUri = tempImagePath
            )
        }

        deleteTarget?.let { target ->
            ConfirmDeleteDialog(
                itemName = target.name,
                onDismiss = { deleteTarget = null },
                onConfirm = {
                    viewModel.deleteMenuItem(target.id)
                    deleteTarget = null
                }
            )
        }

        // Translation confirmation dialog
        showTranslationDialog?.let { targetLang ->
            val languageNames = mapOf(
                "en" to "English",
                "zh-Hans" to "简体中文",
                "fr" to "Français",
                "it" to "Italiano"
            )
            val langName = languageNames[targetLang] ?: targetLang
            val completionRate = uiState.translationCompletionRate
            val isFullyTranslated = completionRate >= 1.0f
            
            AlertDialog(
                onDismissRequest = { showTranslationDialog = null },
                title = { Text(if (isFullyTranslated) "翻訳の再生成" else "一括翻訳") },
                text = { 
                    Column {
                        Text("言語: $langName")
                        Spacer(Modifier.height(8.dp))
                        if (isFullyTranslated) {
                            Text("全てのメニューの翻訳が完了しています。\n翻訳を再生成しますか？")
                        } else {
                            val percent = (completionRate * 100).toInt()
                            Text("現在の翻訳完了率: $percent%")
                            Text("未翻訳のメニューを翻訳しますか？")
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("※Gemini APIを使用します", style = MaterialTheme.typography.bodySmall, color = GrayTextSecondary)
                    }
                },
                confirmButton = {
                    Row {
                        if (isFullyTranslated) {
                            TextButton(
                                onClick = {
                                    viewModel.translateAllMenus(targetLang, forceUpdate = true)
                                    viewModel.selectLanguage(targetLang)
                                    showTranslationDialog = null
                                }
                            ) {
                                Text("再生成する")
                            }
                        } else {
                            TextButton(
                                onClick = {
                                    viewModel.translateAllMenus(targetLang, forceUpdate = false)
                                    viewModel.selectLanguage(targetLang)
                                    showTranslationDialog = null
                                }
                            ) {
                                Text("不足分を翻訳")
                            }
                        }
                    }
                },
                dismissButton = {
                    Row {
                        // Option to force regenerate even if not fully translated (optional, but good for UX)
                        if (!isFullyTranslated && completionRate > 0) {
                             TextButton(
                                onClick = {
                                    viewModel.translateAllMenus(targetLang, forceUpdate = true)
                                    viewModel.selectLanguage(targetLang)
                                    showTranslationDialog = null
                                }
                            ) {
                                Text("全て再生成")
                            }
                        }
                        
                        TextButton(
                            onClick = {
                                // Just switch language without translating
                                viewModel.selectLanguage(targetLang)
                                showTranslationDialog = null
                            }
                        ) {
                            Text("翻訳せずに表示")
                        }
                    }
                }
            )
        }

        // Translation error dialog
        uiState.translationError?.let { error ->
            AlertDialog(
                onDismissRequest = { viewModel.clearTranslationError() },
                title = { Text("翻訳エラー") },
                text = { Text(error) },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearTranslationError() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun MenuItemRow(
    displayItem: DisplayMenuItem,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onToggleAvailability: () -> Unit
) {
    val item = displayItem.originalItem
    
    Box {
        DaCard(
            modifier = Modifier
                .clickable(onClick = onClick)
                .alpha(if (item.isAvailable) 1f else 0.6f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image (if available)
                if (!item.imagePath.isNullOrBlank()) {
                    com.example.restomaster.ui.components.DaLocalImage(
                        imagePath = item.imagePath,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = displayItem.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    // Display description if available
                    if (!displayItem.displayDescription.isNullOrBlank()) {
                        Text(
                            text = displayItem.displayDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = GrayTextSecondary,
                            modifier = Modifier.padding(top = 2.dp),
                            maxLines = 2,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayTextSecondary
                    )
                }
                Text(
                    text = "¥${item.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(Modifier.width(8.dp))
                
                // Availability Toggle
                androidx.compose.material3.Switch(
                    checked = item.isAvailable,
                    onCheckedChange = { onToggleAvailability() },
                    modifier = Modifier.scale(0.8f)
                )
                
                androidx.compose.material3.IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "削除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        
        // Sold out overlay
        if (!item.isAvailable) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                // Diagonal banner
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "売り切れ",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AddMenuDialog(
    initialCategory: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, String?, String?) -> Unit,
    onSelectImage: () -> Unit,
    selectedImageUri: String?
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(initialCategory) }
    var description by remember { mutableStateOf("") }
    // Use selectedImageUri if available, otherwise allow manual input (though manual input of local path is rarely useful)
    var imageUrl by remember(selectedImageUri) { mutableStateOf(selectedImageUri ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("メニュー追加") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("料理名") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("価格") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Simplified category selection for now (just text field or pre-filled)
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("カテゴリ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("説明") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                
                Spacer(Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("画像パス") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        readOnly = true // Make it read-only if we rely on selection
                    )
                    Spacer(Modifier.width(8.dp))
                    androidx.compose.material3.IconButton(onClick = onSelectImage) {
                        Icon(Icons.Default.Image, contentDescription = "Select Image")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val p = price.toIntOrNull()
                    if (name.isNotBlank() && p != null) {
                        onConfirm(
                            name, 
                            p, 
                            category, 
                            description.ifBlank { null }, 
                            imageUrl.ifBlank { null }
                        )
                    }
                }
            ) {
                Text("追加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}

fun copyImageToAppStorage(context: android.content.Context, uri: android.net.Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = "menu_img_${System.currentTimeMillis()}.jpg"
        val file = java.io.File(context.filesDir, fileName)
        val outputStream = java.io.FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
private fun ConfirmDeleteDialog(
    itemName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("削除しますか？") },
        text = { Text("「$itemName」を削除すると元に戻せません。") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("削除")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}
