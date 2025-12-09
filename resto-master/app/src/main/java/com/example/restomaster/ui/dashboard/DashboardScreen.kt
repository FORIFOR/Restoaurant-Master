package com.example.restomaster.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.ui.components.DaCard
import com.example.restomaster.ui.components.DaPrimaryButton
import com.example.restomaster.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateTables: () -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigateReservations: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("レストランマスター") },
            actions = {
                IconButton(onClick = onNavigateSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "設定")
                }
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = spacing.lg, vertical = spacing.md)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "今日の状況",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(spacing.md))

            // テーブル状況概要
            DaCard {
                Text("テーブル数: ${state.tables.size}")
                Spacer(Modifier.height(spacing.sm))
                Text("稼働中の注文: ${state.activeOrders.size}")
            }

            Spacer(Modifier.height(spacing.lg))

            Text(
                text = "ショートカット",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(spacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.md)
            ) {
                DaPrimaryButton(
                    text = "テーブル管理",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateTables
                )
                DaPrimaryButton(
                    text = "メニュー管理",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateMenu
                )
            }

            Spacer(Modifier.height(spacing.md))

            DaPrimaryButton(
                text = "予約一覧",
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateReservations
            )
        }
    }
}
