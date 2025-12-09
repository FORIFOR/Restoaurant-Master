package com.example.restomaster.ui.ocr

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.domain.usecase.ParsedMenuItem
import com.example.restomaster.ui.components.DaCard
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.LocalSpacing
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrResultScreen(
    imageUri: android.net.Uri?,
    viewModel: OcrResultViewModel = hiltViewModel(),
    onSaved: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current
    val context = androidx.compose.ui.platform.LocalContext.current
    var category by remember { mutableStateOf("Food") }

    LaunchedEffect(imageUri) {
        if (imageUri != null && state.parsedItems.isEmpty() && !state.isLoading) {
            viewModel.processImage(imageUri, context)
        }
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("読み取り結果の確認") })
        },
        bottomBar = {
            Column(modifier = Modifier.padding(spacing.lg)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("登録カテゴリ") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(spacing.md))
                DaPrimaryButton(
                    text = "すべて登録",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Copy image to app storage if available
                        val imagePath = imageUri?.let { uri ->
                            com.example.restomaster.ui.menu.copyImageToAppStorage(context, uri)
                        }
                        viewModel.saveAll(category, imagePath)
                    }
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text("解析中...")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = spacing.lg)
            ) {
                state.errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = spacing.sm)
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    itemsIndexed(state.parsedItems) { index, item ->
                        ParsedItemRow(
                            item = item,
                            onUpdate = { newItem -> viewModel.updateItem(index, newItem) },
                            onDelete = { viewModel.deleteItem(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ParsedItemRow(
    item: ParsedMenuItem,
    onUpdate: (ParsedMenuItem) -> Unit,
    onDelete: () -> Unit
) {
    DaCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = item.name,
                    onValueChange = { onUpdate(item.copy(name = it)) },
                    label = { Text("品名") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = item.price.toString(),
                    onValueChange = {
                        val newPrice = it.toIntOrNull() ?: 0
                        onUpdate(item.copy(price = newPrice))
                    },
                    label = { Text("価格") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "削除")
            }
        }
    }
}
