package com.example.restomaster.ui.menu.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.remember
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MenuDetailScreen(
    viewModel: MenuDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current

    var selectedTabIndex by remember { androidx.compose.runtime.mutableIntStateOf(0) }
    val tabs = listOf("基本情報", "翻訳")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.menuItem?.name ?: "メニュー詳細") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (state.menuItem == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                androidx.compose.material3.TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        androidx.compose.material3.Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> BasicInfoTab(
                        state = state,
                        spacing = spacing,
                        viewModel = viewModel,
                        onSave = { 
                            viewModel.onSave()
                            onBack()
                        }
                    )
                    1 -> TranslationTab(
                        state = state,
                        spacing = spacing,
                        onGenerate = { langs -> viewModel.onGenerateTranslations(langs) },
                        onUpdate = { viewModel.onUpdateTranslation(it) },
                        onApprove = { viewModel.onApproveTranslation(it) },
                        onToggleLang = { viewModel.onTranslationLanguageToggle(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BasicInfoTab(
    state: MenuDetailViewModel.DetailUiState,
    spacing: com.example.restomaster.ui.theme.Spacing,
    viewModel: MenuDetailViewModel,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md)
    ) {
        // Menu Info Section
        Text("基本情報", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(spacing.sm))
        Text("価格: ¥${state.menuItem?.price}", style = MaterialTheme.typography.bodyLarge)
        Text("カテゴリ: ${state.menuItem?.category}", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(Modifier.height(spacing.md))
        
        androidx.compose.material3.OutlinedTextField(
            value = state.menuItem?.description ?: "",
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = { Text("説明 (顧客用メニューに表示)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(Modifier.height(spacing.md))

        // Image Section
        Text("料理画像", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(spacing.sm))
        
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            state.menuItem?.imagePath?.let { path ->
                if (path.isNotBlank()) {
                    com.example.restomaster.ui.components.DaLocalImage(
                        imagePath = path,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Spacer(Modifier.width(spacing.md))
                }
            }
            
            val context = androidx.compose.ui.platform.LocalContext.current
            val pickMedia = androidx.activity.compose.rememberLauncherForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    val path = com.example.restomaster.ui.menu.copyImageToAppStorage(context, uri)
                    if (path != null) {
                        viewModel.onImageUpdate(path)
                    }
                }
            }
            
            Column {
                androidx.compose.material3.OutlinedButton(
                    onClick = { 
                        pickMedia.launch(androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(Modifier.width(spacing.xs))
                    Text("画像を変更")
                }
                
                if (!state.menuItem?.imagePath.isNullOrBlank()) {
                    Spacer(Modifier.height(spacing.xs))
                    androidx.compose.material3.FilledTonalButton(
                        onClick = { viewModel.onEnhanceImage() },
                        enabled = !state.isEnhancing
                    ) {
                        if (state.isEnhancing) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        }
                        Spacer(Modifier.width(spacing.xs))
                        Text("AIで補正")
                    }
                } else {
                    Spacer(Modifier.height(spacing.xs))
                    androidx.compose.material3.FilledTonalButton(
                        onClick = { viewModel.onGenerateAiImage() },
                        enabled = !state.isGeneratingImage
                    ) {
                        if (state.isGeneratingImage) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(spacing.xs))
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(Modifier.width(spacing.xs))
                        }
                        Text("AIで生成")
                    }
                }
            }
        }

        // Comparison UI for Enhancement
        state.enhancedImagePath?.let { enhancedPath ->
            Spacer(Modifier.height(spacing.md))
            androidx.compose.material3.Card(
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(spacing.md).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("✨ AI補正案が生成されました", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    Spacer(Modifier.height(spacing.sm))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(), 
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Before", style = MaterialTheme.typography.bodySmall)
                            state.menuItem?.imagePath?.let { original ->
                                com.example.restomaster.ui.components.DaLocalImage(
                                    imagePath = original,
                                    contentDescription = "Original",
                                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                            }
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.rotate(180f)) // Right Arrow
                        
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("After", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                            com.example.restomaster.ui.components.DaLocalImage(
                                imagePath = enhancedPath,
                                contentDescription = "Enhanced",
// Add imports at top of file (I can't do that easily with replace_file_content if I target lines 258/265. I should assume imports might be needed or use the fully qualified static method syntax if I really have to, but better to import).
// Actually, I can replace the content to use the extension function properly if it is imported.
// If it is NOT imported, I need to add import. 
// Let's check if I can add imports.
// I'll try to use the `multi_replace_file_content` to add imports at the top AND fix the lines.

// For now, I will fix the lines. I am assuming standard compose imports are present or I will add them.
// "import androidx.compose.ui.draw.rotate"
// "import androidx.compose.foundation.border"

                                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)).border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(spacing.md))
                    Row {
                       androidx.compose.material3.Button(
                           onClick = { viewModel.onApplyEnhancedImage() },
                           colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                       ) {
                           Text("この補正を採用")
                       }
                       Spacer(Modifier.width(spacing.md))
                       androidx.compose.material3.OutlinedButton(onClick = { viewModel.onDiscardEnhancedImage() }) {
                           Text("破棄")
                       }
                    }
                }
            }
        }
        
        if (state.menuItem?.isAiGenerated == true) {
            Spacer(Modifier.height(spacing.xs))
            Text(
                text = "※画像はイメージです。実際の盛り付けと異なる場合があります",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(spacing.lg))

        // Allergen Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("アレルギー情報 (28品目)", style = MaterialTheme.typography.titleMedium)
            
            if (state.isSuggesting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                androidx.compose.material3.TextButton(onClick = { viewModel.onSuggestAllergens() }) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(Modifier.width(spacing.xs))
                    Text("AIで予測")
                }
            }
        }
        
        Text(
            "※ AI予測は参考情報です。必ず目視で確認してください。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        
        Spacer(Modifier.height(spacing.md))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs)
        ) {
            state.allergens.forEach { allergen ->
                AllergenChip(
                    label = allergen.label,
                    checked = allergen.checked,
                    isAiSuggested = allergen.suggestedByAi,
                    onClick = { viewModel.onAllergenToggle(allergen.allergenCode) }
                )
            }
        }

        Spacer(Modifier.height(spacing.xl))

        DaPrimaryButton(
            text = if (state.isSaving) "保存中..." else "保存",
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TranslationTab(
    state: MenuDetailViewModel.DetailUiState,
    spacing: com.example.restomaster.ui.theme.Spacing,
    onGenerate: (List<String>) -> Unit,
    onUpdate: (com.example.restomaster.data.local.entity.MenuTranslationEntity) -> Unit,
    onApprove: (com.example.restomaster.data.local.entity.MenuTranslationEntity) -> Unit,
    onToggleLang: (String) -> Unit = {}
) {
    val supportedLangs = listOf(
        "en" to "English",
        "zh-Hans" to "简体中文",
        "fr" to "Français",
        "it" to "Italiano"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md)
    ) {
        Text("一括翻訳", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(spacing.sm))
        
        Text("翻訳する言語を選択してください:", style = MaterialTheme.typography.bodySmall)
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalArrangement = Arrangement.spacedBy(spacing.xs)
        ) {
            supportedLangs.forEach { (langCode, langName) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.selectedTranslationLanguages.contains(langCode),
                        onCheckedChange = { onToggleLang(langCode) }
                    )
                    Text(langName)
                }
            }
        }

        Spacer(Modifier.height(spacing.sm))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (state.isTranslating) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                androidx.compose.material3.Button(
                    onClick = { onGenerate(state.selectedTranslationLanguages.toList()) },
                    enabled = state.selectedTranslationLanguages.isNotEmpty()
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(Modifier.width(spacing.xs))
                    Text("選択した言語を翻訳")
                }
            }
        }
        
        Spacer(Modifier.height(spacing.lg))
        androidx.compose.material3.HorizontalDivider()
        Spacer(Modifier.height(spacing.lg))

        supportedLangs.forEach { (langCode, langName) ->
            val translation = state.translations.find { it.lang == langCode }
            
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = spacing.md),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(spacing.md)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(langName, style = MaterialTheme.typography.titleSmall)
                        if (translation != null) {
                            val isApproved = translation.status == "approved"
                            androidx.compose.material3.AssistChip(
                                onClick = { if (!isApproved) onApprove(translation) },
                                label = { Text(if (isApproved) "公開中" else "承認して公開") },
                                leadingIcon = {
                                    if (isApproved) Icon(Icons.Default.Check, contentDescription = null)
                                },
                                colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                                    containerColor = if (isApproved) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                    labelColor = if (isApproved) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    if (translation != null) {
                        Spacer(Modifier.height(spacing.sm))
                        androidx.compose.material3.OutlinedTextField(
                            value = translation.name,
                            onValueChange = { onUpdate(translation.copy(name = it, status = "draft")) },
                            label = { Text("品名") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(spacing.sm))
                        androidx.compose.material3.OutlinedTextField(
                            value = translation.description ?: "",
                            onValueChange = { onUpdate(translation.copy(description = it, status = "draft")) },
                            label = { Text("説明") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    } else {
                        Spacer(Modifier.height(spacing.sm))
                        Text("翻訳はまだありません", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun AllergenChip(
    label: String,
    checked: Boolean,
    isAiSuggested: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = checked,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (checked) {
            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else null,
        trailingIcon = if (isAiSuggested && checked) {
            { Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp)) }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = if (isAiSuggested) 
                MaterialTheme.colorScheme.tertiaryContainer 
            else 
                MaterialTheme.colorScheme.primaryContainer
        )
    )
}
