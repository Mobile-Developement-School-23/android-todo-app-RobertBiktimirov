package com.template.todoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.template.todoapp.R
import com.template.todoapp.ui.main_screen.MainFragment
import com.template.todoapp.ui.task_screen.TaskFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container_view, MainFragment())
            .commit()

    }
}