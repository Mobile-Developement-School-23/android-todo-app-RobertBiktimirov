package com.template.todoapp.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.dataStoreFile
import androidx.lifecycle.ViewModelProvider
import com.template.api.ApiTokenProvider
import com.template.task_feature.ui.task_list_screen.TaskListFragment
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.TaskFragment
import com.template.todoapp.app.appComponent
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.template.resourses_module.R as resR
import com.template.todoapp.R as mainR

class MainActivity : AppCompatActivity(), TaskNavigation {

    private val sharedPreferences by lazy {
        getSharedPreferences(
            getString(resR.string.name_shared_preference),
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(mainR.layout.activity_main)

        if (getToken() == null) {
            val yandexSdk = YandexAuthSdk(this, YandexAuthOptions(this))

            val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
            val intent = yandexSdk.createLoginIntent(loginOptionsBuilder.build())

            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val yandexAuthToken = yandexSdk.extractToken(it.resultCode, it.data)
                    if (yandexAuthToken != null) {
                        saveToken(yandexAuthToken.value)
                    }
                }
            }.launch(intent)
        } else {
            setToken(getToken() ?: throw RuntimeException(""))
        }


        Log.d("getToken Log", getToken().toString())
    }


    private fun saveToken(token: String) {

        with(sharedPreferences.edit()) {
            putString(getString(resR.string.key_sp_token), token)
            apply()
        }

        setToken(token)
    }

    private fun setToken(token: String) {
        ApiTokenProvider.token = token

        supportFragmentManager.beginTransaction()
            .add(mainR.id.main_fragment_container_view, TaskListFragment())
            .commit()
    }

    private fun getToken(): String? =
        sharedPreferences.getString(getString(resR.string.key_sp_token), null)

    override fun onBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun goTaskFragment(todoId: String?) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                resR.anim.slide_in,
                resR.anim.fade_out,
                resR.anim.fade_in,
                resR.anim.slide_out
            )
            .add(mainR.id.main_fragment_container_view, TaskFragment.getNewInstance(todoId))
            .addToBackStack(null)
            .commit()
    }
}