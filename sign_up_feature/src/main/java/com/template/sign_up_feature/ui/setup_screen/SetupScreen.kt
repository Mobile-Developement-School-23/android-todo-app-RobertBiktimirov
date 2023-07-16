package com.template.sign_up_feature.ui.setup_screen

sealed interface Action

sealed interface Ui: Action

object ClickSignUpButton: Ui

sealed interface Internal: Action

object ErrorSignUp: Internal

class SuccessSignUp(val token: String): Internal

object Registered : Internal

data class SignStateScreen(
    var isError: Boolean = false,
    var isLoading: Boolean = false,
    var isRegistered: Boolean = false,
)