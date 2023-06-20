package com.template.todoapp.ui.main_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.todoapp.R
import com.template.todoapp.app.appComponent
import com.template.todoapp.databinding.FragmentMainBinding
import com.template.todoapp.di.viewmodels.ViewModelFactory
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.ui.main_screen.adapter.TaskListAdapter
import com.template.todoapp.ui.main_screen.adapter.TaskListTouchHelper
import com.template.todoapp.ui.task_screen.TaskFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment(), TaskListTouchHelper.SetupTaskBySwipe {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val taskListAdapter by lazy {
        TaskListAdapter(this::adapterChooseHandler, this::adapterInfoHandler)
    }

    private val taskListTouchHelper by lazy {
        TaskListTouchHelper(requireContext(), this)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
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
                viewModel.todoList.collect {
                    viewModel.setIsEmptyList(it.isEmpty())
                    taskListAdapter.submitList(it)
                    setCountDoneTask(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.emptinessTodoList.collect {
                binding.viewEmptyList.isVisible = it
                binding.taskList.isVisible = !it
            }
        }

        binding.addTaskButton.setOnClickListener {
            openSetupTaskScreen(null)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isVisibleDoneTask.collectLatest { flag ->
                    val listItemTodo: List<TodoItem> = if (flag) {
                        viewModel.todoList.value.filter { item -> !item.isCompleted }
                    } else {
                        viewModel.todoList.value
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

    private fun adapterChooseHandler(todoItem: TodoItem) {
        viewModel.updateTodo(todoItem)
    }

    private fun adapterInfoHandler(todoItem: TodoItem) {
        openSetupTaskScreen(todoItem)
    }

    private fun openSetupTaskScreen(todo: TodoItem?) {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            .add(R.id.main_fragment_container_view, TaskFragment.getNewInstance(todo))
            .addToBackStack(null)
            .commit()
    }

    override fun deleteTask(position: Int) {
        viewModel.deleteTodo(taskListAdapter.currentList[position])
    }

    override fun subscribeOnTask(position: Int) {
        taskListAdapter.mapTodoItem[position]?.isCompleted =
            !(taskListAdapter.mapTodoItem[position]?.isCompleted ?: false)
        taskListAdapter.notifyItemChanged(position)
    }

}