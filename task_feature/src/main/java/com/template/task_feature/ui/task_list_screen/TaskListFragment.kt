package com.template.task_feature.ui.task_list_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.resourses_module.R
import com.template.task_feature.databinding.FragmentMainBinding
import com.template.task_feature.di.TaskComponentViewModel
import com.template.task_feature.di.modules.TaskListFragmentComponent
import com.template.task_feature.di.modules.TaskListFragmentViewsComponent
import com.template.task_feature.di.modules.viewmodels.ViewModelFactory
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.ui.task_list_screen.adapter.TaskListAdapter
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.utlis.showSnackbarNoInternet
import com.template.todoapp.ui.main_screen.adapter.TaskListTouchHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskListFragment : Fragment() {
    @Inject
    lateinit var fragmentComponentBuilder: TaskListFragmentComponent.Builder

    lateinit var fragmentComponent: TaskListFragmentComponent
    private var viewComponent: TaskListFragmentViewsComponent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent = fragmentComponentBuilder.fragment(this).build()
        ViewModelProvider(this).get<TaskComponentViewModel>()
            .component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        viewComponent = fragmentComponent.viewsComponentBuilder()
            .binding(binding)
            .build().apply {
                boot()
            }

        return binding.root
    }

    override fun onDestroyView() {
        viewComponent = null
        super.onDestroyView()
    }
}