package com.package.msgapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.package.msgapp.model.Message
import com.package.msgapp.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    fun sendMessage(chatId: String, messageText: String) {
        val senderId = chatRepository.getCurrentUserId() ?: return
        val senderEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        val message = Message(senderId = senderId, senderEmail = senderEmail, messageText = messageText)
        chatRepository.sendMessage(chatId, message)
    }

    fun subscribeToChat(chatId: String) {
        chatRepository.subscribeToMessages(
            chatId = chatId,
            onMessageAdded = { message ->
                viewModelScope.launch {
                    _messages.update { currentList -> currentList + message }
                }
            },
            onMessageRemoved = { messageId ->
                viewModelScope.launch {
                    _messages.update { currentList -> currentList.filter { it.id != messageId } }
                }
            }
        )
    }

    fun deleteMessage(chatId: String, message: Message) {
        message.id?.let { id ->
            chatRepository.deleteMessage(chatId, id)
        }
    }
}
