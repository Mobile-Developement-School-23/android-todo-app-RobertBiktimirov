package com.template.todoapp.ui.main

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.template.common.utli.runCatchingNonCancellation
import com.template.task_feature.ui.task_list_screen.TaskListFragment
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.TaskFragment
import com.template.todoapp.app.appComponent
import com.template.todoapp.databinding.ActivityMainBinding
import com.template.todoapp.ui.network_callback.NetworkConnectivityObserver
import com.template.todoapp.ui.network_callback.availableNetworkCallback
import com.template.todoapp.ui.network_callback.networkRequest
import com.template.todoapp.ui.network_callback.observer.ConnectionObserver
import com.template.todoapp.ui.services.factory.CreateWorkerFactory
import com.template.todoapp.ui.services.factory.WorkerStart
import com.template.todoapp.ui.yandex.YandexSignUpJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.template.resourses_module.R as resR
import com.template.todoapp.R as mainR

class MainActivity : AppCompatActivity(), TaskNavigation {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw Exception()

    @Inject
    lateinit var yandexSignUpActivity: YandexSignUpJob

    @Inject
    lateinit var updateDataWorkerFactory: CreateWorkerFactory

    @Inject
    lateinit var updateDataWorkerStart: WorkerStart

    private val connectivityObserver by lazy {
        NetworkConnectivityObserver(this)
    }

    private fun yandexLauncher(intent: Intent, job: ((it: ActivityResult) -> Unit)) =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            job(it)
        }.launch(intent)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startYandexSignUp()
    }

    private fun startYandexSignUp() {
        runCatchingNonCancellation {
            yandexLauncher(yandexSignUpActivity.getYandexIntent(intent)) {
                yandexSignUpActivity.registerYandexSignUp(
                    it,
                    { handlerStartWorkFragment() },
                    { handleErrorInYandex() }
                )
            }
        }.getOrElse {
            handleErrorInYandex()
        }
    }

    private fun handlerStartWorkFragment() {
        binding.signUpButton.isVisible = false
        updateDataWorkerStart.startUpdateDataWorker()

        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest {
                when (it) {
                    ConnectionObserver.Status.Available -> {
                        updateDataWorkerStart.startLoadNewDataFromDb()
                    }
                    ConnectionObserver.Status.Lost -> {}
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .add(mainR.id.main_fragment_container_view, TaskListFragment())
            .commit()
    }


    private fun handleErrorInYandex() {
        binding.signUpButton.apply {
            isVisible = true
            setOnClickListener {
                startYandexSignUp()
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}