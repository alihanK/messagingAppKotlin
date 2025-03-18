package com.package.msgapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.package.msgapp.repository.ChatRepository

class ChatViewModelFactory(
    private val chatRepository: ChatRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
