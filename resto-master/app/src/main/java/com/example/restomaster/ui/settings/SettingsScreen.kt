package com.example.restomaster.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.restomaster.domain.model.AppMode
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.GrayTextSecondary
import com.example.restomaster.ui.theme.LocalSpacing
import java.io.File

// 12-color theme palette for customer web app
private val themeColorPalette = listOf(
    "#D35400" to "サンセットオレンジ",
    "#C0392B" to "クリムゾンレッド",
    "#27AE60" to "フォレストグリーン",
    "#2980B9" to "オーシャンブルー",
    "#8E44AD" to "ロイヤルパープル",
    "#2C3E50" to "ミッドナイトネイビー",
    "#E74C3C" to "コーラルピンク",
    "#F39C12" to "ゴールデンイエロー",
    "#16A085" to "ティール",
    "#7F8C8D" to "スレートグレー",
    "#9B2335" to "バーガンディ",
    "#5D4037" to "エスプレッソブラウン"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateHandy: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = spacing.lg, vertical = spacing.md)
                .verticalScroll(rememberScrollState())
        ) {
            // Store settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.md)
            ) {
                Column(modifier = Modifier.padding(spacing.md), verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    Text("店舗情報", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = state.storeName,
                        onValueChange = viewModel::onStoreNameChange,
                        label = { Text("店舗名") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.storeInvoiceNumber,
                        onValueChange = viewModel::onStoreInvoiceNumberChange,
                        label = { Text("登録番号 (Tから始まる13桁)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.storePostal,
                        onValueChange = viewModel::onStorePostalChange,
                        label = { Text("郵便番号") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.storeAddress1,
                        onValueChange = viewModel::onStoreAddress1Change,
                        label = { Text("住所1") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.storeAddress2,
                        onValueChange = viewModel::onStoreAddress2Change,
                        label = { Text("住所2") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.storePhone,
                        onValueChange = viewModel::onStorePhoneChange,
                        label = { Text("電話番号") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                    ) {
                        Text("税込価格で運用する", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = state.storeTaxIncluded,
                            onCheckedChange = viewModel::onStoreTaxIncludedChange
                        )
                    }
                    DaPrimaryButton(
                        text = "店舗情報を保存",
                        onClick = { viewModel.onSaveStore() },
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.storeSaveMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                    }
                    state.storeErrorMessage?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            // Customer Web App Customization
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.md)
            ) {
                Column(modifier = Modifier.padding(spacing.md), verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    Text("お客様用注文画面のカスタマイズ", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "QRコードから開く注文画面のテーマカラーとスプラッシュ画像を設定できます。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(spacing.sm))
                    
                    // Splash Image Picker
                    val context = LocalContext.current
                    val imageLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let { viewModel.onSplashImageSelected(context, it) }
                    }
                    
                    Text("スプラッシュ画像", style = MaterialTheme.typography.labelLarge)
                    
                    if (state.splashImagePath != null) {
                        // Show preview with remove button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            AsyncImage(
                                model = File(state.splashImagePath),
                                contentDescription = "Splash Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentScale = ContentScale.Fit
                            )
                            IconButton(
                                onClick = { viewModel.onSplashImagePathChange(null) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "削除",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                        .padding(4.dp)
                                )
                            }
                        }
                    } else {
                        // Show placeholder with upload button
                        OutlinedButton(
                            onClick = { imageLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("画像を選択")
                        }
                    }
                    
                    Text(
                        "推奨サイズ: 400x200px以上、横長の画像",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(Modifier.height(spacing.md))
                    
                    // Theme Color Picker
                    Text("テーマカラー", style = MaterialTheme.typography.labelLarge)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(themeColorPalette) { (colorHex, colorName) ->
                            val isSelected = state.themeColor == colorHex
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorHex.toColorInt()))
                                    .then(
                                        if (isSelected) Modifier.border(3.dp, Color.White, CircleShape)
                                        else Modifier
                                    )
                                    .clickable { viewModel.onThemeColorChange(colorHex) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                    
                    // Current selection display
                    val selectedName = themeColorPalette.find { it.first == state.themeColor }?.second ?: "カスタム"
                    Text(
                        "選択中: $selectedName",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(spacing.md))

                    DaPrimaryButton(
                        text = "テーマ設定を保存",
                        onClick = { viewModel.onSaveStore() },
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.storeSaveMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                    }
                    state.storeErrorMessage?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            // Staff Handy Mode Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.md)
            ) {
                Column(modifier = Modifier.padding(spacing.md)) {
                    Text(
                        text = "スタッフ用ハンディモード",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = spacing.sm)
                    )
                    Text(
                        text = "注文入力専用の簡易画面に切り替えます。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(spacing.md))
                    DaPrimaryButton(
                        text = "ハンディモードへ切り替え",
                        onClick = onNavigateHandy,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Server Control Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.md)
            ) {
                Column(modifier = Modifier.padding(spacing.md)) {
                    Text(
                        text = "モバイルオーダーサーバ",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = spacing.sm)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "注文受付サーバを起動",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = if (state.isServerRunning) "起動中 (Port: 8080)" else "停止中",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (state.isServerRunning) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Switch(
                            checked = state.isServerRunning,
                            onCheckedChange = { viewModel.toggleServer(it) }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.md)
            ) {
                Column(modifier = Modifier.padding(spacing.md)) {
                    Text("Gemini APIキー", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(spacing.sm))
                    Text("現在: ${state.geminiKeyMasked}", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(spacing.sm))
                    OutlinedTextField(
                        value = state.geminiApiKeyInput,
                        onValueChange = viewModel::onGeminiApiKeyChange,
                        label = { Text("Gemini API Key") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(spacing.sm))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        DaPrimaryButton(
                            text = "保存",
                            onClick = { viewModel.onSaveGeminiApiKey() }
                        )
                    }
                    state.geminiSaveMessage?.let { msg ->
                        Spacer(Modifier.height(spacing.xs))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.md)
            ) {
                Column(modifier = Modifier.padding(spacing.md)) {
                    Text("OpenAI APIキー", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(spacing.sm))
                    Text("現在: ${state.openAiKeyMasked}", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(spacing.sm))
                    OutlinedTextField(
                        value = state.openAiApiKeyInput,
                        onValueChange = viewModel::onOpenAiApiKeyChange,
                        label = { Text("OpenAI API Key") },
                        placeholder = { Text("sk-...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(spacing.sm))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        DaPrimaryButton(
                            text = "保存",
                            onClick = { viewModel.onSaveOpenAiApiKey() }
                        )
                    }
                    state.openAiSaveMessage?.let { msg ->
                        Spacer(Modifier.height(spacing.xs))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Debug Actions Section
            Text("端末モード", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(spacing.sm))

            ModeOptionRow(
                title = "ホスト端末（マスター）",
                description = "DBとローカルHTTPサーバを持ち、他端末やお客さんからの注文を受け付けます。",
                selected = state.mode == AppMode.HOST,
                onClick = { viewModel.onModeChange(AppMode.HOST) }
            )
            Spacer(Modifier.height(spacing.sm))
            ModeOptionRow(
                title = "クライアント端末",
                description = "ホスト端末に接続して、注文一覧や会計を行います。",
                selected = state.mode == AppMode.CLIENT,
                onClick = { viewModel.onModeChange(AppMode.CLIENT) }
            )

            Spacer(Modifier.height(spacing.lg))

            if (state.mode == AppMode.CLIENT) {
                Text("ホスト端末の情報", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(spacing.sm))

                OutlinedTextField(
                    value = state.hostIpInput,
                    onValueChange = viewModel::onHostIpChange,
                    label = { Text("ホストIPアドレス") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(spacing.sm))
                OutlinedTextField(
                    value = state.hostPortInput,
                    onValueChange = viewModel::onHostPortChange,
                    label = { Text("ポート番号") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(spacing.sm))
                Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                    DaPrimaryButton(
                        text = "保存",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onSaveHostInfo() }
                    )
                    DaPrimaryButton(
                        text = if (state.isTestingConnection) "接続テスト中..." else "接続テスト",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onTestConnection() }
                    )
                }

                Spacer(Modifier.height(spacing.sm))
                when (state.connectionStatus) {
                    SettingsViewModel.ConnectionStatus.Success ->
                        Text("接続OK", color = Color(0xFF2E7D32))
                    SettingsViewModel.ConnectionStatus.Failure ->
                        Text("接続できませんでした", color = MaterialTheme.colorScheme.error)
                    else -> {}
                }
            } else {
                Text("この端末はホスト端末として動作します。", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(spacing.sm))
                Text(
                    "Wi-FiテザリングをONにし、この端末のIPアドレスとポート（例: 192.168.43.1:8080）を他端末に設定してください。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayTextSecondary
                )
            }
        }
    }
}

@Composable
private fun ModeOptionRow(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .padding(spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.width(spacing.sm))
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(spacing.xs))
            Text(description, style = MaterialTheme.typography.bodySmall, color = GrayTextSecondary)
        }
    }
}
