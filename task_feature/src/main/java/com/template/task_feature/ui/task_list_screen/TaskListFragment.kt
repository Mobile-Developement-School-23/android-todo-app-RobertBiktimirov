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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.resourses_module.R
import com.template.task_feature.databinding.FragmentMainBinding
import com.template.task_feature.di.TaskComponentViewModel
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.ui.task_list_screen.adapter.TaskListAdapter
import com.template.task_feature.ui.task_list_screen.adapter.TaskListTouchHelper
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.utlis.showSnackbarNoInternet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskListFragment : Fragment(), TaskListTouchHelper.SetupTaskBySwipe {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    private var navigation: TaskNavigation? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        observersData()
        respondToView()
    }

    private fun respondToView() {

        binding.settingButton.setOnClickListener {
            navigation?.goSettingFragment()
        }

        binding.addTaskButton.setOnLongClickListener {
            startAnimForAddButton()
            true
        }

        binding.addTaskButton.setOnClickListener {
            openSetupTaskScreen(null)
        }


        binding.isVisibleDoneTask.setOnClickListener {
            binding.isVisibleDoneTask.isActivated = !binding.isVisibleDoneTask.isActivated
            viewModel.isVisibleDone = binding.isVisibleDoneTask.isActivated
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.updateTodoList()
        }
    }

    private fun observersData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoList.collect { todoShell ->
                    viewModel.setIsEmptyList(todoShell.todoItem.isEmpty())
                    taskListAdapter.submitList(
                        todoShell.todoItem
                    )
                    setCountDoneTask(todoShell.todoItem)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.emptinessTodoList.collect {
                binding.viewEmptyList.isVisible = it
                binding.taskList.isVisible = !it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.startAddButtonAnim.collectLatest {
                viewModel.setStateAnim(false)
                if (it) {
                    startAnimForAddButton()
                }
            }
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

        viewLifecycleOwner.lifecycleScope.launch {
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
}