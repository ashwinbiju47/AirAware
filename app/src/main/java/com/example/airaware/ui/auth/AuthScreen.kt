package com.example.airaware.ui.auth

import android.util.Patterns
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.airaware.R
import com.example.airaware.data.local.User

@Composable
fun AuthScreen(
    onAuthSuccess: (User) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val message by viewModel.authMessage

    fun validate(): Boolean {
        if (email.isBlank() || password.isBlank()) {
            error = "Email and password cannot be empty."
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = "Invalid email format."
            return false
        }
        if (password.length < 6) {
            error = "Password must be at least 6 characters."
            return false
        }
        if (isSignUp && password != confirmPassword) {
            error = "Passwords do not match."
            return false
        }
        return true
    }

    val neonGreen = MaterialTheme.colorScheme.primary       // now Sky Blue
    val background = MaterialTheme.colorScheme.background   // light sky
    val textColor = MaterialTheme.colorScheme.onBackground  // deep blue
    val onPrimary = MaterialTheme.colorScheme.onPrimary     // white text on buttons

    // ---- Animated Sky Gradient Background ----
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB3E5FC), // top sky blue
                        Color(0xFFE1F5FE), // soft mid blue
                        Color(0xFFFFFFFF)  // near white bottom
                    ),
                    startY = offset,
                    endY = offset + 1000f
                )
            )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Logo (optional)
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(90.dp)
                        .padding(bottom = 12.dp)
                )

                // App name
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = neonGreen,
                        fontSize = 28.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(32.dp))

                // Boxy form container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, neonGreen, RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSignUp) "CREATE ACCOUNT" else "LOGIN",
                        style = MaterialTheme.typography.titleMedium.copy(color = neonGreen),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = neonGreen) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = neonGreen) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isSignUp) {
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password", color = neonGreen) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Sky blue action button
                    Button(
                        onClick = {
                            error = null
                            if (!validate()) return@Button
                            if (isSignUp) {
                                viewModel.register(email, password)
                            } else {
                                viewModel.login(email, password) { user ->
                                    onAuthSuccess(user)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = neonGreen,
                            contentColor = onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isSignUp) "Sign Up" else "Sign In",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = onPrimary,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    TextButton(onClick = {
                        isSignUp = !isSignUp
                        error = null
                    }) {
                        Text(
                            if (isSignUp) "Already registered? Access system"
                            else "New user? Create account",
                            color = neonGreen,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    error?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    message?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            it,
                            color = neonGreen.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
