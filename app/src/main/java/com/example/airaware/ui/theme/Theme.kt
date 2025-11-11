package com.example.airaware.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val MatrixLightColorScheme = lightColorScheme(
    primary = MatrixGreen,
    secondary = MatrixGreenSecondary,
    tertiary = MatrixGreen,
    background = MatrixDarkBackground,
    surface = MatrixSurface,
    onPrimary = MatrixOnPrimary,
    onSecondary = MatrixOnPrimary,
    onBackground = MatrixOnBackground,
    onSurface = MatrixOnBackground,
    error = MatrixError
)

@Composable
fun AirAwareTheme(
    darkTheme: Boolean = false,    // ☀ Always light and airy
    dynamicColor: Boolean = false, // Disable system dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> MatrixLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
