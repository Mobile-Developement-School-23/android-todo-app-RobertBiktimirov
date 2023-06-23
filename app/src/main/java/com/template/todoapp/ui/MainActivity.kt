package com.template.todoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.template.todoapp.R as mainR
import com.template.resourses_module.R as resR
import com.template.todoapp.app.appComponent
import com.template.task_feature.ui.main_screen.MainFragment
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.TaskFragment

class MainActivity : AppCompatActivity(), TaskNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(mainR.layout.activity_main)


        supportFragmentManager.beginTransaction()
            .add(mainR.id.main_fragment_container_view, MainFragment())
            .commit()

    }

    override fun onBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun goTaskFragment(todoId: String?) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(resR.anim.slide_in, resR.anim.fade_out, resR.anim.fade_in, resR.anim.slide_out)
            .add(mainR.id.main_fragment_container_view, TaskFragment.getNewInstance(todoId))
            .addToBackStack(null)
            .commit()
    }
}