package com.package.msgapp.repository

import com.package.msgapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) {
    fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    private fun getMessagesRef(chatId: String): DatabaseReference =
        firebaseDatabase.getReference("chats").child(chatId)

    fun sendMessage(chatId: String, message: Message) {
        getMessagesRef(chatId).push().setValue(message)
    }

    fun subscribeToMessages(
        chatId: String,
        onMessageAdded: (Message) -> Unit,
        onMessageRemoved: (String) -> Unit
    ) {
        getMessagesRef(chatId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Message::class.java)?.let { msg ->
                    onMessageAdded(msg.copy(id = snapshot.key))
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.key?.let { key ->
                    onMessageRemoved(key)
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun deleteMessage(chatId: String, messageId: String) {
        getMessagesRef(chatId).child(messageId).removeValue()
    }
}
