package com.template.task_feature.data.setup_notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.template.task_feature.domain.entity.TodoItem
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager

    override fun schedule(todoItem: TodoItem) {

//        val intent = Intent(context, AlarmReceiver::class.java).apply {
//            putExtra(
//                context.getString(com.template.resourses_module.R.string.key_alarm_manager_intent),
//                todoItem.id
//            )
//        }
//
//        todoItem.deadline?.let {
//            alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                it,
//                PendingIntent.getBroadcast(
//                    context,
//                    todoItem.id.hashCode(),
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                )
//            )
//        }
    }

    override fun cancel(todoItem: TodoItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                todoItem.id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}