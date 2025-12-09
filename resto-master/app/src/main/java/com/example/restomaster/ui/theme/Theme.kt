package com.example.restomaster.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BluePrimaryLight,
    onPrimaryContainer = BluePrimaryDark, // Better contrast
    secondary = StatusOccupied,
    onSecondary = Color.White,
    tertiary = StatusAlert,
    onTertiary = Color.White,
    background = GrayBackground,
    onBackground = GrayTextMain,
    surface = GraySurface,
    onSurface = GrayTextMain,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = GrayTextSecondary,
    error = ErrorRed,
    onError = Color.White,
    outline = GrayBorder
)

// We focus on Light Theme for now as per Digital Agency style (high contrast)
// But we can add Dark Theme later if needed.

@Composable
fun RestoMasterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic color to enforce design system
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Force Light Scheme for now to match design system request
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false // Dark status bar icons on light background? No, primary is dark blue, so icons should be light.
        }
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography, // We will update Typography next
            shapes = Shapes(
                small = RoundedCornerShape(4.dp),
                medium = RoundedCornerShape(6.dp),
                large = RoundedCornerShape(8.dp)
            ),
            content = content
        )
    }
}
