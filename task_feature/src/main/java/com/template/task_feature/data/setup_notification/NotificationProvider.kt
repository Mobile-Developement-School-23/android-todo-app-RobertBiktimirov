package com.template.task_feature.data.setup_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.template.task_feature.domain.entity.Importance
import javax.inject.Inject
import com.template.resourses_module.R as resR

class NotificationProvider @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
    }


    fun showNotification(importance: Importance, task: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val text = when (importance) {
            Importance.LOW -> "Низкая важность"
            Importance.REGULAR -> "Нету важности"
            Importance.URGENT -> "Высокая важность!!"
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(text)
            .setContentText(task)
            .setSmallIcon(resR.drawable.notebook__1_)
            .setAutoCancel(true)
            .addAction(
                NotificationCompat.Action.Builder(
                    resR.drawable.baseline_clear_24,
                    context.getString(resR.string.postpone),
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                ).build()
            )

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}
