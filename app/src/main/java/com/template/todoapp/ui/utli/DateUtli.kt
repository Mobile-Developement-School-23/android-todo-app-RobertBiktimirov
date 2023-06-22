package com.template.todoapp.ui.utli

import java.text.SimpleDateFormat
import java.util.*

fun Long?.toFormatDate(): String {
    val date = Date(this ?: 0)
    val format = SimpleDateFormat("d MMM yyyy", Locale("ru"))
    return format.format(date)
}