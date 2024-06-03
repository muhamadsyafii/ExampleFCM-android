package id.syafii.examplefcm.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Icon
import android.media.AudioAttributes
import android.media.AudioAttributes.Builder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.syafii.examplefcm.R
import java.lang.Exception

class FcmService : FirebaseMessagingService() {
  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    val title = remoteMessage.notification?.title
    val message = remoteMessage.notification?.body
    Log.d("CheckFirebase", "onMessageReceived: title -  $title")
    Log.d("CheckFirebase", "onMessageReceived: message - $message")
    showNotification(
      title = title ?: "",
      message = message ?: ""
    )

//    if (remoteMessage.data.keys.size > 0) {
//      val title = remoteMessage.data["title"]
//      val message = remoteMessage.data["body"]
//      Log.d("CheckFirebase", "onMessageReceived: title -  $title")
//      Log.d("CheckFirebase", "onMessageReceived: message - $message")
////      showNotification(title ?: "", message ?: "", type ?: "")
//    }
    Log.d("CheckFirebase", "onMessageReceived: $remoteMessage")
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    Log.d("CheckFirebase", "onNewToken: $token")
  }

  override fun onSendError(msgId: String, exception: Exception) {
    super.onSendError(msgId, exception)
    Log.d("CheckFirebase", "onSendError: ${exception.message}")

  }

  private fun showNotification(
    title: String,
    message: String,
    notificationStyle: NotificationCompat.Style? = null,
    resultPendingIntent: PendingIntent? = null
  ) {
    val sound = Uri.parse("android.resource://$packageName")

    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher))
      .setLargeIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
      .setSound(sound)
      .setColor(ContextCompat.getColor(this.applicationContext, R.color.primary_red))
      .setContentTitle(title)
      .setContentText(message)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setAutoCancel(true)
      .setStyle(notificationStyle)
      .setContentIntent(resultPendingIntent)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val attributes = Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()
      val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
      notificationChannel.enableLights(true)
      notificationChannel.lightColor = Color.RED
      notificationChannel.enableVibration(true)
      notificationChannel.setShowBadge(true)
      notificationChannel.setSound(sound, attributes)
      val notificationManager = NotificationManagerCompat.from(this)
      notificationManager.createNotificationChannel(notificationChannel)
    }

    val notificationManager = NotificationManagerCompat.from(this)
    val notificationId = (System.currentTimeMillis() / 4).toInt()
    notificationManager.notify(notificationId, builder.build())

  }


  companion object {
    private const val TAG = "Firebase"
    private const val NOTIFY_ID = 39147
    private const val CHANNEL_ID = "39147"
    private const val CHANNEL_NAME = "Aplikasi Pesanan Online"
    private const val CHANNEL_DESC = "Channel notifikasi untuk aplikasi pesanan online"



  }
}