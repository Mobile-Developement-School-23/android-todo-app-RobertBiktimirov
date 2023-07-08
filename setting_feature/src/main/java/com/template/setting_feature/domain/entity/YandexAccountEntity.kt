package com.template.setting_feature.domain.entity

data class YandexAccountEntity(
    val name: String,
    val login: String,
    val email: String,
    val birthday: String,
    var avatarId: String,
    val phone: String,
)
