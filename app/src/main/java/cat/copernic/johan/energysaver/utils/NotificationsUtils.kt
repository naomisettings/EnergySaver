package cat.copernic.johan.energysaver.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import cat.copernic.johan.energysaver.R
import java.security.AccessControlContext
import java.security.AccessController.getContext

class NotificationsUtils {

    val NOTIFICATION_ID = 0

    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notificacions_canal)
            )   .setSmallIcon(R.drawable.raig)
                .setContentTitle(applicationContext.getString(R.string.notificacions_titol))
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notify(NOTIFICATION_ID, builder.build())

    }
}