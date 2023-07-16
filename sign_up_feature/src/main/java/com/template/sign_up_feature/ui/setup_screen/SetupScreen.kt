package com.template.sign_up_feature.ui.setup_screen

import com.yandex.authsdk.YandexAuthToken

sealed interface Action

sealed interface Ui: Action

object ClickSignUpButton: Ui

sealed interface Internal: Action

object ErrorSignUp: Internal

class SuccessSignUp(val token: YandexAuthToken): Internal

object Registered : Internal

data class SignStateScreen(
    var isError: Boolean = false,
    var isLoading: Boolean = false,
    var isRegistered: Boolean = false,
)