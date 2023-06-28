package com.template.task_feature.ui.task_list_screen

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.template.task_feature.di.modules.viewmodels.ViewModelFactory
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.ui.task_list_screen.adapter.TaskListAdapter
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.todoapp.ui.main_screen.adapter.TaskListTouchHelper
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskListFragment : Fragment(), TaskListTouchHelper.SetupTaskBySwipe {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    private var navigation: TaskNavigation? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TaskListViewModel::class.java]
    }


    private val taskListAdapter by lazy {
        TaskListAdapter(this::adapterChooseHandler, this::adapterInfoHandler)
    }

    private val taskListTouchHelper by lazy {
        TaskListTouchHelper(requireContext(), this)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<TaskComponentViewModel>()
            .component.inject(this)

        if (context is TaskNavigation) {
            navigation = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoList.collectIndexed { index, it ->
                    Log.d("api test save todoItem", it.toString())
                    viewModel.setIsEmptyList(it.todoItem.isEmpty())
                    taskListAdapter.submitList(it.todoItem)
                    setCountDoneTask(it.todoItem)

                    binding.myTaskTitle.text = if (index == FIRST_LOAD_INDEX) {
                        getString(R.string.title_update)
                    } else {
                        getString(R.string.my_tasks)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.emptinessTodoList.collect {
                binding.viewEmptyList.isVisible = it
                binding.taskList.isVisible = !it

                if (it) {
                    startAnimForAddButton()
                }

            }
        }

        binding.addTaskButton.setOnLongClickListener {
            startAnimForAddButton()
            true
        }

        binding.addTaskButton.setOnClickListener {
            openSetupTaskScreen(null)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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

        binding.isVisibleDoneTask.setOnClickListener {
            binding.isVisibleDoneTask.isActivated = !binding.isVisibleDoneTask.isActivated
            viewModel.isVisibleDone = binding.isVisibleDoneTask.isActivated
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

    private fun setCountDoneTask(todoList: List<TodoItem>) {
        val count = todoList.filter { it.isCompleted }.size.toString()
        binding.doneCount.text = String.format(getString(R.string.done_task), count)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        navigation = null
        super.onDetach()
    }

    private fun adapterChooseHandler(todoItem: TodoItem) {
        viewModel.updateTodo(todoItem)
    }

    private fun adapterInfoHandler(todoItem: TodoItem) {
        openSetupTaskScreen(todoItem.id)
    }

    private fun openSetupTaskScreen(todoId: String?) {
        navigation?.goTaskFragment(todoId)
    }

    override fun deleteTask(position: Int) {
        viewModel.deleteTodo(taskListAdapter.currentList[position])
    }

    override fun subscribeOnTask(position: Int) {
        taskListAdapter.mapTodoItem[position]?.isCompleted =
            !(taskListAdapter.mapTodoItem[position]?.isCompleted ?: false)
        taskListAdapter.notifyItemChanged(position)
    }


    companion object {
        private const val FIRST_LOAD_INDEX = 1
    }

}