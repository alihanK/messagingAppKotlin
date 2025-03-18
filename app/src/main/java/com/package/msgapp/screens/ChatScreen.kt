package com.package.msgapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.package.msgapp.MyApplication
import com.package.msgapp.model.Message
import com.package.msgapp.viewmodels.ChatViewModel
import com.package.msgapp.viewmodels.ChatViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId: String, onLogout: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as MyApplication
    val chatRepository = app.appComponent.getChatRepository()
    val factory = ChatViewModelFactory(chatRepository)
    val chatViewModel: ChatViewModel = viewModel(factory = factory)
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(chatId) {
        chatViewModel.subscribeToChat(chatId)
    }

    val messages by chatViewModel.messages.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Messaging App") },
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        containerColor = Color(0xFFFFEBEE) // Arka plan rengini açık kırmızı ayarladım.
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(messages) { message ->
                    MessageItem(
                        message = message,
                        currentUserId = currentUserId,
                        onDelete = { chatViewModel.deleteMessage(chatId, message) }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Write a message..") },
                    modifier = Modifier
                        .weight(1f),
                    shape = RoundedCornerShape(8.dp)  // Rounding
                )
                IconButton(onClick = {
                    if (inputText.isNotBlank()) {
                        chatViewModel.sendMessage(chatId, inputText)
                        inputText = ""
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "SEND")
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, currentUserId: String?, onDelete: () -> Unit) {
    val formattedDate = remember(message.timestamp) {
        SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(message.timestamp))
    }

    // Helper function to mask email address (only first 2 chars visible before '@')
    fun maskEmail(email: String): String {
        val parts = email.split("@")
        return if (parts.size == 2) {
            val localPart = parts[0]
            val domain = parts[1]
            if (localPart.length >= 2) localPart.take(2) + "..@" + domain else localPart + "..@" + domain
        } else email
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color.Cyan),
                    startY = 0f,
                    endY = 200f
                ),
                shape = RoundedCornerShape(12.dp)  // Mesaj kutusunun köşeleri yuvarlatıldı
            )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = message.messageText,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall
            )
        }
        if (message.senderEmail.isNotBlank()) {
            Text(
                text = maskEmail(message.senderEmail),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            )
        }
        if (message.senderId == currentUserId) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
