package com.template.setting_feature.data.mappers

import com.template.api.entity.YandexAccountApi
import com.template.database.entity.YandexAccountDto
import com.template.setting_feature.domain.entity.YandexAccountEntity

fun YandexAccountApi.toEntity(): YandexAccountEntity = YandexAccountEntity(
    name,
    login,
    email,
    birthday,
    avatarId,
    phoneInfo.number
)

fun YandexAccountDto.toEntity(): YandexAccountEntity = YandexAccountEntity(
    name,
    login,
    email,
    birthday,
    avatarId,
    phone
)


fun YandexAccountEntity.toDto(): YandexAccountDto = YandexAccountDto(
    name,
    login,
    email,
    birthday,
    avatarId,
    phone
)