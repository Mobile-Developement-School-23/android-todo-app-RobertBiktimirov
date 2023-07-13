package com.template.task_feature.data.setup_notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.template.database.dao.TodoDao
import com.template.task_feature.data.mappers.toUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var todoDao: TodoDao

    @Inject
    lateinit var notificationProvider: NotificationProvider

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onReceive(context: Context?, intent: Intent?) {

        val todoItemId = intent?.getStringExtra(
            context?.getString(com.template.resourses_module.R.string.key_alarm_manager_intent)
        ) ?: return

        coroutineScope.launch {
            val todoItem = todoDao.getTodoItem(todoItemId)
            notificationProvider.showNotification(todoItem.importance.toUi(), todoItem.text)
        }

    }
}