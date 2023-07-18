package com.template.todoapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.template.common.utli.runCatchingNonCancellation
import com.template.setting_feature.ui.SettingFragment
import com.template.sign_up_feature.ui.SignFragment
import com.template.task_feature.ui.task_list_screen.TaskListFragment
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.TaskFragment
import com.template.todoapp.app.appComponent
import com.template.todoapp.databinding.ActivityMainBinding
import com.template.todoapp.domain.entity.ThemeEnum
import com.template.todoapp.ui.NavigationApp
import com.template.todoapp.ui.network_callback.NetworkConnectivityObserver
import com.template.todoapp.ui.network_callback.observer.ConnectionObserver
import com.template.todoapp.ui.services.factory.CreateWorkerFactory
import com.template.todoapp.ui.services.factory.WorkerStart
import com.template.todoapp.ui.token.TokenProvider
import com.template.todoapp.ui.yandex.YandexSignUpJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.template.resourses_module.R as resR
import com.template.todoapp.R as mainR

class MainActivity : AppCompatActivity(), NavigationApp {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw Exception()

    @Inject
    lateinit var yandexSignUpActivity: YandexSignUpJob

    @Inject
    lateinit var tokenProvider: TokenProvider

    @Inject
    lateinit var updateDataWorkerFactory: CreateWorkerFactory

    @Inject
    lateinit var updateDataWorkerStart: WorkerStart

    private val connectivityObserver by lazy {
        NetworkConnectivityObserver(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .add(mainR.id.main_fragment_container_view, SignFragment())
                .commit()
        }

        connectivityObserver()
        themeObserver()

    }


    private fun connectivityObserver() {

        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                when (it) {
                    ConnectionObserver.Status.Available -> {
                        connectivityObserver.lastState?.let { lastStatus ->
                            if (lastStatus == ConnectionObserver.Status.Lost) {
                                updateDataWorkerStart.startLoadNewDataFromDb()
                            }
                        }
                    }

                    ConnectionObserver.Status.Lost -> {
                        connectivityObserver.lastState = ConnectionObserver.Status.Lost
                    }
                }
            }
        }

    }


    private fun themeObserver() {
        runCatchingNonCancellation {
            lifecycleScope.launch {
                viewModel.themeFlow.collect {
                    when (it) {
                        ThemeEnum.DARK -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }

                        ThemeEnum.DAY -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }

                        ThemeEnum.SYSTEM -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                    }
                }
            }
        }
    }

    override fun goToListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(mainR.id.main_fragment_container_view, TaskListFragment())
            .commit()
    }

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

    override fun goSettingFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                resR.anim.slide_in,
                resR.anim.fade_out,
                resR.anim.fade_in,
                resR.anim.slide_out
            )
            .addToBackStack(null)
            .add(mainR.id.main_fragment_container_view, SettingFragment())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateDataWorkerStart.startPeriodicUpdateData()
        _binding = null
    }
}