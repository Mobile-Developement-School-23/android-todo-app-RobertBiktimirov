package com.template.todoapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.template.task_feature.ui.task_list_screen.TaskListFragment
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.TaskFragment
import com.template.todoapp.app.appComponent
import com.template.todoapp.ui.update_data_service.UpdateDataWorkerFactory
import com.template.todoapp.ui.update_data_service.UpdateDataWorkerStart
import com.template.todoapp.ui.yandex.YandexSignUpActivity
import javax.inject.Inject
import com.template.resourses_module.R as resR
import com.template.todoapp.R as mainR

class MainActivity : AppCompatActivity(), TaskNavigation {

    @Inject
    lateinit var yandexSignUpActivity: YandexSignUpActivity

    @Inject
    lateinit var updateDataWorkerFactory: UpdateDataWorkerFactory

    @Inject
    lateinit var updateDataWorkerStart: UpdateDataWorkerStart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(mainR.layout.activity_main)


        yandexSignUpActivity.registerYandexSignUp {
            supportFragmentManager.beginTransaction()
                .add(mainR.id.main_fragment_container_view, TaskListFragment())
                .commit()

            updateDataWorkerStart.startUpdateDataWorker()
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
}