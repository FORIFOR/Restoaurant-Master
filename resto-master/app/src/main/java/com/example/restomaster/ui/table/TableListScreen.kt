package com.example.restomaster.ui.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.ui.components.DaCard
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.GrayTextSecondary
import com.example.restomaster.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableListScreen(
    viewModel: TableListViewModel = hiltViewModel()
) {
    val tableListState by viewModel.tableListUiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current
    val context = androidx.compose.ui.platform.LocalContext.current

    val qrPreviewState by viewModel.qrPreviewState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("テーブル管理") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onClickAddTable() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "テーブル追加")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = spacing.lg, vertical = spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.sm)
        ) {
            items(tableListState.items, key = { it.table.id }) { item ->
                val table = item.table
                val isOccupied = item.activeOrder != null
                val statusColor = when (table.status) {
                    "CHECKOUT_REQUESTED" -> MaterialTheme.colorScheme.errorContainer
                    "PAYMENT_IN_PROGRESS" -> MaterialTheme.colorScheme.tertiaryContainer
                    "PAID" -> androidx.compose.ui.graphics.Color(0xFFE8F5E9)
                    else -> if (isOccupied) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                }
                
                DaCard(
                    onClick = { /* Navigate to detail */ },
                    containerColor = statusColor
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("テーブル ${table.tableNumber}", style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.size(spacing.sm))
                                
                                when (table.status) {
                                    "CHECKOUT_REQUESTED" -> {
                                        androidx.compose.material3.Badge(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ) {
                                            Text("会計待", color = MaterialTheme.colorScheme.onError)
                                        }
                                    }
                                    "PAYMENT_IN_PROGRESS" -> {
                                        androidx.compose.material3.Badge(
                                            containerColor = MaterialTheme.colorScheme.tertiary
                                        ) {
                                            Text("決済中", color = MaterialTheme.colorScheme.onTertiary)
                                        }
                                    }
                                    "PAID" -> {
                                        androidx.compose.material3.Badge(
                                            containerColor = androidx.compose.ui.graphics.Color(0xFF2E7D32)
                                        ) {
                                            Text("支払済", color = androidx.compose.ui.graphics.Color.White)
                                        }
                                    }
                                    else -> {
                                        if (isOccupied) {
                                            androidx.compose.material3.Badge(
                                                containerColor = MaterialTheme.colorScheme.secondary
                                            ) {
                                                Text("使用中", color = MaterialTheme.colorScheme.onSecondary)
                                            }
                                        } else {
                                            androidx.compose.material3.Badge(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                            ) {
                                                Text("空席")
                                            }
                                        }
                                    }
                                }
                            }
                            Text(
                                "トークン: ${table.token.take(6)}…",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrayTextSecondary
                            )
                            if (isOccupied) {
                                Text(
                                    "現在のお会計: ¥${item.totalAmount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        DaPrimaryButton(
                            text = "QR発行",
                            onClick = { 
                                viewModel.generateQr(table)
                            }
                        )
                    }
                }
            }
        }

        // Add Table Dialog
        if (uiState.isAddDialogVisible) {
            AddTableDialog(
                state = uiState,
                onDismiss = { viewModel.onDismissAddDialog() },
                onNumberChange = { viewModel.onChangeTableNumberInput(it) },
                onConfirm = { viewModel.onConfirmAddTable() }
            )
        }

        // QR Preview Dialog
        if (qrPreviewState != null) {
            QrPreviewDialog(
                bitmap = qrPreviewState!!.bitmap,
                onDismiss = { viewModel.clearQrPreview() },
                onShare = {
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
            )
        }
    }
}

@Composable
private fun AddTableDialog(
    state: com.example.restomaster.ui.table.TableListViewModel.UiState,
    onDismiss: () -> Unit,
    onNumberChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    val spacing = LocalSpacing.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("テーブル追加") },
        text = {
            Column {
                Text("追加するテーブル番号を入力してください")
                Spacer(Modifier.height(spacing.sm))
                OutlinedTextField(
                    value = state.newTableNumberInput,
                    onValueChange = onNumberChange,
                    label = { Text("テーブル番号") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = state.errorMessage != null
                )
                if (state.errorMessage != null) {
                    Spacer(Modifier.height(spacing.xs))
                    Text(
                        text = state.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            DaPrimaryButton(
                text = if (state.isSaving) "保存中..." else "追加",
                onClick = onConfirm,
                modifier = Modifier
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}

@Composable
fun QrPreviewDialog(
    bitmap: android.graphics.Bitmap,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("QRコードプレビュー") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.foundation.Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(200.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                Text("このQRコードを印刷してテーブルに配置してください。")
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onShare) {
                Text("共有・保存")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("閉じる")
            }
        }
    )
}
