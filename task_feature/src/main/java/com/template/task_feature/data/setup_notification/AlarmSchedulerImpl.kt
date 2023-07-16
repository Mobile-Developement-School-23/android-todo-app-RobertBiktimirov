package com.template.task_feature.data.setup_notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.template.task_feature.domain.entity.TodoItem
import javax.inject.Inject
import com.template.resourses_module.R

class AlarmSchedulerImpl @Inject constructor(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager

    override fun schedule(todoItem: TodoItem) {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(
                context.getString(R.string.key_alarm_manager_intent),
                Bundle().apply {
                    putParcelable(
                        context.getString(R.string.key_alarm_manager_todo_item_key),
                        todoItem
                    )
                }
            )
        }

        todoItem.deadline?.let {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                it,
                PendingIntent.getBroadcast(
                    context,
                    todoItem.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
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