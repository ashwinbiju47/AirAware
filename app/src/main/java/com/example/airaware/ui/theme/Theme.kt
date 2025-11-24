package com.example.airaware.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val AirLightColorScheme = lightColorScheme(
    primary = AirPrimary,
    secondary = AirSecondary,
    tertiary = AirPrimary,
    background = AirBackground,
    surface = AirSurface,
    onPrimary = AirOnPrimary,
    onSecondary = AirOnPrimary,
    onBackground = AirOnBackground,
    onSurface = AirOnBackground,
    error = AirError
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
        else -> AirLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
