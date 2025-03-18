package com.package.msgapp.model

data class Message(
    val id: String? = null,
    val senderId: String = "",
    val senderEmail: String = "", // Yeni: Mesajı gönderen kullanıcının email adresi
    val messageText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
