package com.example.airaware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.airaware.ui.AppRoot
import com.example.airaware.ui.theme.AirAwareTheme   // ✅ import your custom theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirAwareTheme(    // ✅ apply your green eco theme here
                darkTheme = true,   // or true / isSystemInDarkTheme()
                dynamicColor = false // disable wallpaper colors
            ) {
                AppRoot()
            }
        }
    }
}
