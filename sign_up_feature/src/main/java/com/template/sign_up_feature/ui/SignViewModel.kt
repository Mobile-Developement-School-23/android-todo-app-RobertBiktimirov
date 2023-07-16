package com.template.sign_up_feature.ui

import android.util.Log
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
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.Dispatchers
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

    private val _stateSignScreen = MutableStateFlow(SignStateScreen())
    val stateSignScreen = _stateSignScreen.asStateFlow()


    private val _isSuccessRegistered = MutableStateFlow(false)
    val isSuccessRegistered = _isSuccessRegistered.asStateFlow()


    init {

        viewModelScope.launch {
            when (val token = getTokenUseCase()) {
                null -> {

                }

                else -> {
                    Log.d("nullTokenTest", token)
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
                _isSuccessRegistered.update { false }
                _stateSignScreen.update {
                    it.copy(isError = true, isLoading = false, isRegistered = false)
                }
            }

            is SuccessSignUp -> {
                saveToken(action.token)
            }

            Registered -> {
                _isSuccessRegistered.update { true }
            }
        }
    }


    private fun saveToken(token: YandexAuthToken){
        viewModelScope.launch(Dispatchers.IO) {
            val newToken = saveTokenUseCase.invoke(token)
            if (newToken != "") {
                onAction(Registered)
            }
        }
    }
}