package com.example.restomaster.ui.handy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restomaster.domain.model.TableStatusUi
import com.example.restomaster.ui.theme.StatusEmpty
import com.example.restomaster.ui.theme.StatusOccupied

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandyTableListScreen(
    onTableClick: (Long) -> Unit,
    onSwitchToHost: () -> Unit,
    viewModel: HandyTableListViewModel = hiltViewModel()
) {
    val tables by viewModel.tables.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff Handy Mode") },
                actions = {
                    IconButton(onClick = onSwitchToHost) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Exit Handy Mode")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Select Table",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tables) { table ->
                    HandyTableCard(
                        table = table,
                        onClick = { onTableClick(table.tableId) }
                    )
                }
            }
        }
    }
}

@Composable
fun HandyTableCard(
    table: TableStatusUi,
    onClick: () -> Unit
) {
    val backgroundColor = if (table.isOccupied) StatusOccupied else StatusEmpty
    val contentColor = Color.White

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${table.tableNumber}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = if (table.isOccupied) "Occupied" else "Empty",
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
