package com.template.sign_up_feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.sign_up_feature.domain.usecases.GetTokenUseCase
import com.template.sign_up_feature.domain.usecases.SaveTokenUseCase
import com.template.sign_up_feature.ui.setup_screen.Action
import com.template.sign_up_feature.ui.setup_screen.ClickSignUpButton
import com.template.sign_up_feature.ui.setup_screen.ErrorSignUp
import com.template.sign_up_feature.ui.setup_screen.Registered
import com.template.sign_up_feature.ui.setup_screen.SignStateScreen
import com.template.sign_up_feature.ui.setup_screen.SuccessSignUp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _goToYandexSignUpAct: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goToYandexSignUpAct = _goToYandexSignUpAct.asStateFlow()

    private val _startAdditionalProtection = MutableStateFlow(false)
    val startAdditionalProtection = _startAdditionalProtection.asStateFlow()

    private val _stateSignScreen = MutableStateFlow(SignStateScreen())
    val stateSignScreen = _stateSignScreen.asStateFlow()


    init {

        viewModelScope.launch {
            when (getTokenUseCase()) {
                null -> {

                }

                else -> {
                    onAction(Registered)
                }
            }
        }

    }


    fun onAction(action: Action) {

        when (action) {
            ClickSignUpButton -> {
                _goToYandexSignUpAct.update {
                    true
                }
            }

            ErrorSignUp -> {
                _stateSignScreen.update {
                    it.copy(isError = true, isLoading = false, isRegistered = false)
                }
            }

            is SuccessSignUp -> {
                saveToken(action.token)
            }

            Registered -> {
                _startAdditionalProtection.update {
                    true
                }
            }
        }
    }


    private fun saveToken(token: String) {
        viewModelScope.launch {
            saveTokenUseCase(token)
            _startAdditionalProtection.update { true }
        }
    }

}