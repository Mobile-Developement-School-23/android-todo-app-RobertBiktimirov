package com.template.task_feature.di.modules

import android.view.MotionEvent
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.resourses_module.R
import com.template.task_feature.databinding.FragmentMainBinding
import com.template.task_feature.databinding.FragmentTaskBinding
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.ui.task_list_screen.TaskListViewModel
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.TaskViewModel
import com.template.task_feature.ui.utlis.showSnackbarNoInternet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewScope
class TaskListViewController @Inject constructor(
    private val binding: FragmentMainBinding,
    private val navigation: TaskNavigation,
    private val viewModel: TaskListViewModel,
    @ViewLifecycleOwner
    private val lifecycleOwner: LifecycleOwner,
) {
    init {
        initUi()
        observersData()
        respondToView()
    }


    private fun respondToView() {
        binding.addTaskButton.setOnLongClickListener {
            startAnimForAddButton()
            true
        }

        binding.addTaskButton.setOnClickListener {
            navigation?.goTaskFragment(null)
        }


        binding.isVisibleDoneTask.setOnClickListener {
            binding.isVisibleDoneTask.isActivated = !binding.isVisibleDoneTask.isActivated
            viewModel.isVisibleDone = binding.isVisibleDoneTask.isActivated
        }
    }

    private fun observersData() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoList.collect {
                    viewModel.setIsEmptyList(it.todoItem.isEmpty())
                    taskListAdapter.submitList(it.todoItem.toSet().toMutableList())
                    setCountDoneTask(it.todoItem)
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            viewModel.emptinessTodoList.collect {
                binding.viewEmptyList.isVisible = it
                binding.taskList.isVisible = !it

                if (it) {
                    startAnimForAddButton()
                }

            }
        }

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isVisibleDoneTask.collectLatest { flag ->
                    val listItemTodo: List<TodoItem> = if (flag) {
                        viewModel.todoList.value.todoItem.filter { item -> !item.isCompleted }
                    } else {
                        viewModel.todoList.value.todoItem
                    }
                    taskListAdapter.submitList(listItemTodo)
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            viewModel.noInternet.collect {
                if (it) {
                    binding.root.showSnackbarNoInternet {}
                }
            }
        }
    }

    private fun startAnimForAddButton() {
        binding.addTaskButton.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.shaking_add_task_button
            )
        )
    }

    private fun initUi() {
        binding.taskList.run {
            adapter = taskListAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            ).apply {
                reverseLayout = true
                stackFromEnd = true
            }
        }


        val itemTouchHelper = ItemTouchHelper(taskListTouchHelper)
        itemTouchHelper.attachToRecyclerView(binding.taskList)

        binding.taskList.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                taskListTouchHelper.clearState()
            }
            false
        }

    }
}