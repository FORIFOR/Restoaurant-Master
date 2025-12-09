package com.example.restomaster.ui.receipt

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.domain.model.ReceiptData
import com.example.restomaster.ui.theme.LocalSpacing
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptPreviewScreen(
    billId: Long,
    onBack: () -> Unit,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val spacing = LocalSpacing.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("レシート") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    if (uiState.receiptData != null) {
                        IconButton(onClick = {
                            viewModel.createShareIntent()?.let { intent ->
                                context.startActivity(Intent.createChooser(intent, "レシートを共有"))
                            }
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "共有")
                        }
                        IconButton(onClick = {
                            viewModel.printWithSystemService(context)
                        }) {
                            Icon(Icons.Default.Print, contentDescription = "印刷")
                        }
                        IconButton(onClick = { viewModel.printWithEscPos() }) {
                            Icon(Icons.Default.Print, contentDescription = "ESC/POS印刷")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(spacing.md))
                        Text("レシートを生成中...")
                    }
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error ?: "エラーが発生しました",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(spacing.md))
                        Button(onClick = { viewModel.retry() }) {
                            Text("再試行")
                        }
                    }
                }
            }

            uiState.receiptData != null -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(spacing.md)
                ) {
                    // Receipt Preview Card
                    ReceiptPreviewCard(receiptData = uiState.receiptData!!)

                    Spacer(Modifier.height(spacing.lg))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.createShareIntent()?.let { intent ->
                                    context.startActivity(android.content.Intent.createChooser(intent, "レシートを共有"))
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("共有")
                        }
                        
                        Button(
                            onClick = {
                                viewModel.printWithSystemService(context)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Print, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("印刷 / 保存")
                        }
                        OutlinedButton(
                            onClick = { viewModel.printWithEscPos() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Print, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("ESC/POS")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptPreviewCard(receiptData: ReceiptData) {
    val numberFormat = NumberFormat.getInstance(Locale.JAPAN)
    val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Store Name
        Text(
            text = receiptData.storeName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Address
        if (receiptData.storeAddress.isNotBlank()) {
            Text(
                text = receiptData.storeAddress,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
        }

        // Phone
        if (!receiptData.storePhone.isNullOrBlank()) {
            Text(
                text = "TEL: ${receiptData.storePhone}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }

        // Invoice Registration Number
        if (!receiptData.invoiceRegistrationNumber.isNullOrBlank()) {
            Text(
                text = "登録番号: ${receiptData.invoiceRegistrationNumber}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(spacing.sm))
        HorizontalDivider(color = Color.LightGray)
        Spacer(Modifier.height(spacing.sm))

        // Title
        Text(
            text = "領　収　書",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(Modifier.height(spacing.sm))
        HorizontalDivider(color = Color.LightGray)
        Spacer(Modifier.height(spacing.sm))

        // Date and Receipt Number
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = dateFormat.format(Date(receiptData.issuedAt)),
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "No. ${receiptData.receiptNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }

        Spacer(Modifier.height(spacing.md))

        // Line Items
        receiptData.items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val itemName = if (item.isReducedTax) "${item.name}※" else item.name
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "¥${numberFormat.format(item.lineTotal)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier.width(64.dp),
                    textAlign = TextAlign.End
                )
            }
            Spacer(Modifier.height(4.dp))
        }

        Spacer(Modifier.height(spacing.sm))
        HorizontalDivider(color = Color.LightGray)
        Spacer(Modifier.height(spacing.sm))

        // Tax Breakdown
        val tb = receiptData.taxBreakdown
        if (tb.subtotal10 > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "10%対象",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
                Text(
                    text = "¥${numberFormat.format(tb.subtotal10 + tb.tax10)} (税 ¥${numberFormat.format(tb.tax10)})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
        if (tb.subtotal8 > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "8%対象※",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
                Text(
                    text = "¥${numberFormat.format(tb.subtotal8 + tb.tax8)} (税 ¥${numberFormat.format(tb.tax8)})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(Modifier.height(spacing.sm))
        HorizontalDivider(color = Color.LightGray)
        Spacer(Modifier.height(spacing.sm))

        // Total
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "合計",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "¥${numberFormat.format(receiptData.totalAmount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "（内消費税）",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            Text(
                text = "¥${numberFormat.format(tb.totalTax)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }

        // Payment Method
        if (!receiptData.paymentMethod.isNullOrBlank()) {
            Spacer(Modifier.height(spacing.sm))
            Row(modifier = Modifier.fillMaxWidth()) {
                val paymentLabel = when (receiptData.paymentMethod) {
                    "CASH" -> "現金"
                    "STRIPE" -> "クレジットカード"
                    "PAYPAY" -> "PayPay"
                    "LINEPAY" -> "LINE Pay"
                    "MIXED" -> "複合払い"
                    else -> receiptData.paymentMethod
                }
                Text(
                    text = "お支払い: $paymentLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(Modifier.height(spacing.sm))
        HorizontalDivider(color = Color.LightGray)
        Spacer(Modifier.height(spacing.sm))

        // Reduced tax note
        if (receiptData.items.any { it.isReducedTax }) {
            Text(
                text = "※は軽減税率対象商品です",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(spacing.md))

        // Thank you message
        Text(
            text = "ありがとうございました",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )
    }
}
