package com.template.todoapp.di

import android.content.Context
import com.template.todoapp.app.TodoApplication
import com.template.todoapp.ui.MainActivity
import com.template.todoapp.ui.main_screen.MainFragment
import com.template.todoapp.ui.task_screen.TaskFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelModule::class]
)
interface AppComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(taskFragment: TaskFragment)
    fun inject(mainActivity: MainActivity)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

}