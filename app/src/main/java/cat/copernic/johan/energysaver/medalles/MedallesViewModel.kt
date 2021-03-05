package cat.copernic.johan.energysaver.medalles

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import cat.copernic.johan.energysaver.R
import cat.copernic.johan.energysaver.utils.cancelNotifications

class MedallesViewModel(private val app: Application) : AndroidViewModel(app) {

    private fun reciveNotification(){
        // TODO: Step 1.15 call cancel notification
        val notificationManager =
            ContextCompat.getSystemService(
                app,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()

    }



}