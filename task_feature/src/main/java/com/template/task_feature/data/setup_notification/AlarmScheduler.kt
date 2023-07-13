package com.template.task_feature.data.setup_notification

import com.template.task_feature.domain.entity.TodoItem

interface AlarmScheduler {

    fun schedule(todoItem: TodoItem)
    fun cancel(todoItem: TodoItem)

}