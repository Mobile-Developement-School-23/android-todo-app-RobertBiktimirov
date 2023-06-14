package com.template.todoapp.ui.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.todoapp.R
import com.template.todoapp.databinding.FragmentMainBinding
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.ui.main_screen.adapter.TaskListAdapter
import com.template.todoapp.ui.task_screen.TaskFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val taskListAdapter by lazy {
        TaskListAdapter(this::adapterChooseHandler, this::adapterInfoHandler)
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoList.collect {
                    taskListAdapter.submitList(it.reversed())

                    binding.doneCount.text = String.format(
                        getString(R.string.done_task),
                        it.filter { tFlag -> tFlag.flag }.size.toString()
                    )
                }
            }
        }

        binding.addTaskButton.setOnClickListener {
            openSetupTaskScreen(null)
        }
    }

    private fun initUi() {
        binding.taskList.adapter = taskListAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun adapterChooseHandler(todoItem: TodoItem) {
        viewModel.editTodo(todoItem)
    }

    private fun adapterInfoHandler(todoItem: TodoItem) {
        openSetupTaskScreen(todoItem)
    }

    private fun openSetupTaskScreen(todo: TodoItem?) {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.main_fragment_container_view, TaskFragment.getNewInstance(todo))
            .commit()
    }

}