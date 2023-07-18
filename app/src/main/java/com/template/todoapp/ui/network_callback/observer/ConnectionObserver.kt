package com.template.todoapp.ui.network_callback.observer

import kotlinx.coroutines.flow.Flow

interface ConnectionObserver {

    val lastState: Status?

    fun observe(): Flow<Status>

    enum class Status {
        Available, Lost
    }
}