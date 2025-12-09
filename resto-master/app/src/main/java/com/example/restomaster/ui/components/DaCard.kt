package com.example.restomaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.restomaster.ui.theme.GrayBorder
import com.example.restomaster.ui.theme.LocalSpacing

@Composable
fun DaCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .background(containerColor)
        .border(1.dp, GrayBorder, MaterialTheme.shapes.medium)
        .padding(LocalSpacing.current.md)

    if (onClick != null) {
        Surface(
            modifier = cardModifier.clickable(onClick = onClick),
            color = Color.Transparent
        ) {
            Column(content = content)
        }
    } else {
        Surface(
            modifier = cardModifier,
            color = Color.Transparent
        ) {
            Column(content = content)
        }
    }
}
