package com.package.msgapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.package.msgapp.model.Message

fun showNotification(context: Context, message: Message) {
    val channelId = "chat_channel"
    val channelName = "Chat Messages"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_email) // Android'in hazır simgesi
        .setContentTitle("Yeni Mesaj")
        .setContentText(message.messageText)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Android 13 ve üzeri için POST_NOTIFICATIONS runtime izni gerekiyor.
    val notificationManagerCompat = NotificationManagerCompat.from(context)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManagerCompat.notify(message.timestamp.toInt(), builder.build())
        }
       
    } else {
        notificationManagerCompat.notify(message.timestamp.toInt(), builder.build())
    }
}
