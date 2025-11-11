package com.example.airaware.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import com.example.airaware.data.local.User
import com.example.airaware.ui.auth.AuthScreen
import com.example.airaware.ui.home.HomeScreen
import com.example.airaware.ui.splash.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppRoot() {
    var loggedInUser by remember { mutableStateOf<User?>(null) }
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(300) // Show splash for 1 seconds
        showSplash = false
    }

    AnimatedVisibility(
        visible = showSplash,
        exit = fadeOut(animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000)) // 👈 fade-out
    ) {
        SplashScreen()
    }

    if (!showSplash) {
        if (loggedInUser == null) {
            AuthScreen(onAuthSuccess = { user -> loggedInUser = user })
        } else {
            HomeScreen(user = loggedInUser!!, onLogout = { loggedInUser = null })
        }
    }
}
