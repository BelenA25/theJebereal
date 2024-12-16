package com.jebereal.jeberealapp.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@SuppressLint("MissingPermission")
fun showNotification(context: Context) {
    // Create NotificationChannel for Android 8.0+ (API 26+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "default_channel",
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "This is a default notification channel."
        }


        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }


    // Create the notification
    val notification = NotificationCompat.Builder(context, "default_channel")
        .setContentTitle("Compra exitosa!")
        .setContentText("Se realizo correctamente la compra de entradas")
        .setSmallIcon(android.R.drawable.ic_dialog_info) // Set your icon here
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true) // Automatically dismiss after user taps
        .build()


    // Show the notification
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1, notification)
}
