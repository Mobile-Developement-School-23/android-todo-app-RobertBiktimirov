package com.template.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("yandex_account")
data class YandexAccountDto(
    @ColumnInfo("real_name") val name: String,
    @ColumnInfo("login") val login: String,
    @PrimaryKey @ColumnInfo("default_email") val email: String,
    @ColumnInfo("birthday") val birthday: String,
    @ColumnInfo("default_avatar_id") var avatarId: Long,
    @ColumnInfo("default_phone") val phone: String,
)
