package com.package.msgapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.package.msgapp.MyApplication
import com.package.msgapp.viewmodels.AuthState
import com.package.msgapp.viewmodels.AuthViewModel
import com.package.msgapp.viewmodels.AuthViewModelFactory

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as MyApplication
    val authRepository = app.appComponent.getAuthRepository()
    val factory = AuthViewModelFactory(authRepository)
    val authViewModel: AuthViewModel = viewModel(factory = factory)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center, // İçerik dikey ortalanır
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Messaging App",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)  // Yuvarlatıldı
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp)  // Yuvarlatıldı
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { authViewModel.register(email, password) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { onNavigateToLogin() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Login")
        }
        when (authState) {
            is AuthState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            is AuthState.Error -> {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            is AuthState.Success -> {
                LaunchedEffect(Unit) { onRegisterSuccess() }
            }
            else -> {}
        }
    }
}
