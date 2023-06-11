package com.template.todoapp.ui.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.todoapp.R
import com.template.todoapp.databinding.FragmentMainBinding
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.ui.main_screen.adapter.TaskListAdapter
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
            viewModel.todoList.collect {
                taskListAdapter.submitList(it)
                binding.doneCount.text = String.format(
                    getString(R.string.done_task),
                    it.map { tFlag -> tFlag.flag }.size.toString()
                )
                Log.d("task_list_test", it.toString())
            }
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

    }

    private fun adapterInfoHandler(todoItem: TodoItem) {

    }
}