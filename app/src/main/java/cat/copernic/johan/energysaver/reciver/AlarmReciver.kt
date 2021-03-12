package cat.copernic.johan.energysaver.reciver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.utils.sendNotification

class AlarmReciver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //Enviar notificació
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            context.getText(R.string.medalles).toString(),
            context
        )

    }
}