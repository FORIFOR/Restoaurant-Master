package com.example.restomaster.ui.table.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableDetailScreen(
    tableId: Long,
    onBack: () -> Unit,
    viewModel: TableDetailViewModel = hiltViewModel(),
    onNavigateReceipt: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val qrPreviewState by viewModel.qrPreviewState.collectAsState()
    val navReceipt by viewModel.navigateToReceipt.collectAsState()
    val spacing = LocalSpacing.current
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(navReceipt) {
        if (navReceipt != null) {
            onNavigateReceipt(navReceipt!!)
            viewModel.onNavigatedReceipt()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("テーブル詳細") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
        } else if (uiState.table == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Table not found")
            }
        } else {
            val table = uiState.table!!
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = spacing.md)
            ) {
                Spacer(Modifier.height(spacing.md))
                
                // Header Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(spacing.md)) {
                        Text(
                            text = "${table.tableNumber}番卓",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "人数: ${uiState.activeOrder?.peopleCount ?: 0}名",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (uiState.stayMinutes != null) {
                                Text(
                                    text = "滞在: ${uiState.stayMinutes}分",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (uiState.activeOrder != null) {
                            val order = uiState.activeOrder!!
                            Text(
                                text = "開始: ${java.text.SimpleDateFormat("HH:mm").format(java.util.Date(order.createdAt))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(Modifier.height(spacing.md))
                
                // Call Requests
                if (uiState.callRequests.isNotEmpty()) {
                    Text("呼び出し・会計依頼", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(spacing.sm))
                    uiState.callRequests.forEach { req ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = spacing.sm),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Row(
                                modifier = Modifier.padding(spacing.md).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (req.type == "checkout") Icons.Default.Payment else Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = Color(0xFFD32F2F)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = if (req.type == "checkout") "会計依頼" else "店員呼び出し",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFFD32F2F)
                                    )
                                }
                                androidx.compose.material3.OutlinedButton(
                                    onClick = { viewModel.onResolveCall(req.id) }
                                ) {
                                    Text("対応完了")
                                }
                        }
                    }
                }
                }
                
                Spacer(Modifier.height(spacing.md))
                
                // Order Items Section
                if (uiState.activeOrder != null && uiState.orderItems.isNotEmpty()) {
                    Text("注文商品", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(spacing.sm))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(Modifier.padding(spacing.md)) {
                            uiState.orderItems.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = spacing.xs),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.menuName,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = "¥${item.menuPrice} × ${item.orderItem.quantity}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    
                                    if (item.orderItem.status == "served") {
                                        Text(
                                            text = "提供済み",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF4CAF50)
                                        )
                                    } else {
                                        androidx.compose.material3.OutlinedButton(
                                            onClick = { viewModel.onMarkItemServed(item.orderItem.id) }
                                        ) {
                                            Text("提供済みにする")
                                        }
                                    }
                                }
                                
                                if (uiState.orderItems.last() != item) {
                                    androidx.compose.material3.Divider(Modifier.padding(vertical = spacing.xs))
                                }
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(spacing.lg))
                    
                    // Checkout Button
                    var showCheckoutConfirm by remember { mutableStateOf(false) }
                    DaPrimaryButton(
                        text = "会計を確定する",
                        onClick = { showCheckoutConfirm = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showCheckoutConfirm) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showCheckoutConfirm = false },
                            title = { Text("会計を確定しますか？") },
                            text = { Text("伝票を作成し会計を完了します。戻せません。") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showCheckoutConfirm = false
                                    viewModel.onCheckoutDone()
                                }) { Text("確定する") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showCheckoutConfirm = false }) { Text("キャンセル") }
                            }
                        )
                    }
                }
            }
        }
        
        // QR Preview Dialog
        if (qrPreviewState != null) {
            // We need a bitmap for the dialog. 
            // Since we only generated a File in ViewModel, we need to render it or change the dialog.
            // For now, let's assume we can't easily render PDF to Bitmap here without a library.
            // But TableListViewModel did it by generating Bitmap AND PDF.
            // Let's modify ViewModel to generate Bitmap too or just use a simple placeholder bitmap for now
            // and rely on the Share intent which uses the PDF file.
            
            // Actually, let's fix ViewModel to generate Bitmap as well.
            // But for now, to unblock, we will just show a text dialog if bitmap is missing,
            // OR we can use the QrCodeGenerator again here.
            
            // Let's use a simple alert dialog for now that allows sharing.
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { viewModel.onDismissQrPreview() },
                title = { Text("QRコード発行") },
                text = { Text("QRコードのPDFを生成しました。") },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            val file = qrPreviewState!!.pdfFile
                            val uri = androidx.core.content.FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            )
                            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(android.content.Intent.createChooser(intent, "QRコードを共有"))
                        }
                    ) {
                        Text("共有・印刷")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { viewModel.onDismissQrPreview() }) {
                        Text("閉じる")
                    }
                }
            )
        }
    }
}
