package com.package.msgapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.package.msgapp.screens.ChatScreen
import com.package.msgapp.screens.LoginScreen
import com.package.msgapp.screens.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "chat" else "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = { navController.navigate("chat") },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = { navController.navigate("chat") },
                                onNavigateToLogin = { navController.navigate("login") }
                            )
                        }
                        composable("chat") {
                            // ChatScreen: logout callback navigasyonu login ekranına yönlendirir.
                            ChatScreen(chatId = "default_chat", onLogout = { navController.navigate("login") })
                        }
                    }
                }
            }
        }
    }
}
