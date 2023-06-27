package com.template.common.utli

import java.text.SimpleDateFormat
import java.util.*

fun Long?.timestampToFormattedDate(): String {
    val date = Date((this  ?: 0) * 1000)
    val format = SimpleDateFormat("d MMM yyyy", Locale("ru"))
    return format.format(date)
}