package com.example.restomaster.ui.refund

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restomaster.data.remote.api.RefundApi
import com.example.restomaster.data.remote.model.refund.RefundItem

import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundListScreen(
    baseUrl: String,
    orderId: String
) {
    val api = remember { RefundApi(baseUrl) }
    val vm = remember { RefundListViewModel(api, orderId) }
    val state by vm.state.collectAsState()

    var showRefundDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.loadInitial()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Refunds") },
                actions = {
                    TextButton(
                        onClick = { showRefundDialog = true },
                        enabled = !state.isRefunding
                    ) {
                        if (state.isRefunding) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Refund")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            state.error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
            state.refundError?.let {
                Text(
                    text = "Refund error: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
            state.refundSuccess?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(12.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.items, key = { it.merchantRefundId }) { r ->
                    RefundRow(r)
                    HorizontalDivider()
                }

                item {
                    if (state.nextCursor != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Button(
                                onClick = { vm.loadMore() },
                                enabled = !state.isLoading,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text("Load more")
                            }
                        }
                    }
                }
            }
        }

        if (showRefundDialog) {
            RefundInputDialog(
                onSubmit = { amount ->
                    showRefundDialog = false
                    vm.refundPartial(amount)
                },
                onDismiss = { showRefundDialog = false }
            )
        }
    }
}

@Composable
private fun RefundRow(r: RefundItem) {
    Column(modifier = Modifier.padding(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(r.merchantRefundId, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.weight(1f))
            Text("¥${r.amountJpy ?: 0}", style = MaterialTheme.typography.titleSmall)
        }
        Text("status: ${r.status ?: "-"}", style = MaterialTheme.typography.bodySmall)
        Text("reason: ${r.reason ?: "-"}", style = MaterialTheme.typography.bodySmall)

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("requested: ${r.requestedAt ?: 0}", style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.weight(1f))
            Text("accepted: ${r.acceptedAt ?: 0}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun RefundInputDialog(
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Partial refund (JPY)") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.filter { ch -> ch.isDigit() } },
                placeholder = { Text("e.g. 300") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val amount = text.toIntOrNull() ?: 0
                if (amount > 0) onSubmit(amount)
            }) { Text("Submit") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
