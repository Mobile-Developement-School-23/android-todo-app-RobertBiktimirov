package com.template.task_feature.data.setup_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.template.resourses_module.R
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.domain.entity.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onReceive(context: Context?, intent: Intent?) {
        val todoItem = intent?.getBundleExtra(
            context?.getString(R.string.key_alarm_manager_intent)
        )?.getParcelable<TodoItem>(context?.getString(R.string.key_alarm_manager_intent))

        coroutineScope.launch {

            todoItem?.let {
                context?.let { it1 -> showNotification(it1, it.importance, it.text) }
            }
        }
    }


    private fun showNotification(context: Context, importance: Importance, task: String) {
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
            .setSmallIcon(R.drawable.notebook__1_)
            .setAutoCancel(true)
            .addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.baseline_clear_24,
                    context.getString(R.string.postpone),
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