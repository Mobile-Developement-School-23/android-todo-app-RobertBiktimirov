package com.template.setting_feature.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.common.utli.RepositoryError
import com.template.common.utli.RepositoryException
import com.template.common.utli.RepositorySuccess
import com.template.setting_feature.domain.entity.YandexAccountEntity
import com.template.setting_feature.domain.usecases.GetDataYandexAccountUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val getDataYandexAccountUseCase: GetDataYandexAccountUseCase
): ViewModel() {


    private val _accountData: MutableStateFlow<YandexAccountEntity?> = MutableStateFlow(null)
    val accountData = _accountData.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    init {
        getDataYandexAccount()
    }

    private fun getDataYandexAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val data = getDataYandexAccountUseCase()) {
                is RepositoryError -> {
                    _error.emit(true)
                    Log.d("getDataYandexAccount", "RepositoryError")
                }

                is RepositoryException -> {
                    _error.emit(true)
                    Log.d("getDataYandexAccount", "RepositoryException")
                }

                is RepositorySuccess -> {
                    _accountData.emit(data.data)
                    Log.d("getDataYandexAccount", "${data.data}")
                }
            }
        }
    }


}