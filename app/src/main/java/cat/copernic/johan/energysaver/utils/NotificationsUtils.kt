package cat.copernic.johan.energysaver.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import cat.copernic.johan.energysaver.MainActivity
import cat.copernic.johan.energysaver.R
import java.security.AccessControlContext
import java.security.AccessController.getContext


// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    // Crear un intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // Crear un pending Intent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Afegir estils
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.raigpetit
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)


    val snoozeIntent = Intent(
        applicationContext,
        cat.copernic.johan.energysaver.reciver.MedallesReciver::class.java
    )
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )

    // Construir la notificació
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.energy_notification_channel_id)
    )

        //Canviar el títol i la icona
        .setSmallIcon(R.drawable.raigpetit)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)


        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)


        .addAction(
            R.drawable.medalla,
            applicationContext.getString(R.string.medalles),
            snoozePendingIntent
        )

        //La prioritat
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    //Trucar a la notificacó
    notify(NOTIFICATION_ID, builder.build())
}

//Cancelar notificacions
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

