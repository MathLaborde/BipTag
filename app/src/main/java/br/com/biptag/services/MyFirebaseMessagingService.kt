package br.com.biptag.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import br.com.biptag.MainActivity
import br.com.biptag.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        android.util.Log.d("FCM_SERVICE", "Novo Token Gerado: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        android.util.Log.d("FCM_SERVICE", "Mensagem recebida: ${remoteMessage.data}")

        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "BipTag"
        val message = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Alguém interagiu com seu item!"
        val foundReportId = remoteMessage.data["foundReportId"]
        val type = remoteMessage.data["type"]
        val returnProcessId = remoteMessage.data["returnProcessId"]

        showNotification(title, message, foundReportId, type, returnProcessId)
    }

    private fun showNotification(title: String, message: String, foundReportId: String?, type: String?, returnProcessId: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "biptag_alerts"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Alertas de Itens", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val uri = when (type) {
            "RETURN_INSTRUCTION" -> {
                if (returnProcessId != null) "biptag://return_instruction/$returnProcessId".toUri()
                else "biptag://home".toUri()
            }
            "ITEM_FOUND" -> {
                if (foundReportId != null) "biptag://item_found/$foundReportId".toUri()
                else "biptag://home".toUri()
            }
            else -> {
                "biptag://home".toUri()
            }
        }

        val intent = Intent(
            Intent.ACTION_VIEW,
            uri,
            this,
            MainActivity::class.java
        )

        val notificationId = (0..100000).random()

        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}