package com.example.airaware.ui

import androidx.compose.runtime.*
import com.example.airaware.data.local.User
import com.example.airaware.ui.auth.AuthScreen
import com.example.airaware.ui.home.HomeScreen

@Composable
fun AppRoot() {
    var loggedInUser by remember { mutableStateOf<User?>(null) }

    if (loggedInUser == null) {
        AuthScreen(onAuthSuccess = { user -> loggedInUser = user })
    } else {
        HomeScreen(user = loggedInUser!!, onLogout = { loggedInUser = null })
    }
}
