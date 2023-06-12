package com.template.todoapp.ui.task_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.template.todoapp.R
import com.template.todoapp.databinding.FragmentTaskBinding
import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.ui.task_screen.spinner_adapter.SpinnerAdapter
import com.template.todoapp.ui.utli.toFormatDate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    private val viewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.deadlineCalendar.isVisible = false

        initSpinner()

        arguments?.takeIf { it.containsKey(KEY_ARGS_TASK) }?.apply {
            viewModel.todoItem.tryEmit(getParcelable(KEY_ARGS_TASK))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todoItem.collectLatest { todoItemNull ->
                todoItemNull?.let { todoItem ->
                    changeUi(todoItem)
                }
            }
        }

        binding.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.deadlineDate.isVisible = isChecked
            binding.deadlineCalendar.isVisible = isChecked

            if (isChecked) {
                if (binding.deadlineDate.text == "") {
                    binding.deadlineDate.text = binding.deadlineCalendar.date.toFormatDate()
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            Toast.makeText(requireContext(), "Имитация выхода", Toast.LENGTH_SHORT).show()
        }

        binding.saveTask.setOnClickListener {
            Toast.makeText(requireContext(), "Имитация сохранения", Toast.LENGTH_SHORT).show()
            viewModel.saveTask(binding.importanceSpinner.selectedItem.toString())
        }

        binding.closeButton.setOnClickListener {
            Toast.makeText(requireContext(), "Имитация выхода", Toast.LENGTH_SHORT).show()
        }

        binding.deadlineCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            binding.deadlineDate.text = GregorianCalendar(
                year,
                month,
                dayOfMonth
            ).timeInMillis.toFormatDate()
        }

        binding.editTextTask.doAfterTextChanged {
            viewModel.setTaskText(it.toString())
        }


    }

    private fun initSpinner() {
        val spinnerAdapter = SpinnerAdapter(
            requireContext(),
            resources.getStringArray(R.array.spinner_values)
        )
        binding.importanceSpinner.adapter = spinnerAdapter
        binding.importanceSpinner.setSelection(0)
    }

    private fun changeUi(todoItem: TodoItem) {
        with(binding) {
            editTextTask.setText(todoItem.text)
            when (todoItem.importance) {
                Importance.LOW -> binding.importanceSpinner.setSelection(0)
                Importance.REGULAR -> binding.importanceSpinner.setSelection(1)
                Importance.URGENT -> binding.importanceSpinner.setSelection(0)
            }

            deadlineDate.isVisible = todoItem.deadline != null
            deadlineDate.text = todoItem.deadline.toFormatDate()
            deadlineSwitch.isChecked = todoItem.deadline != null

            if (todoItem.deadline != null) {
                deadlineCalendar.date = todoItem.deadline
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    companion object {

        private const val KEY_ARGS_TASK = "args"

        fun getNewInstance(task: TodoItem?): TaskFragment = TaskFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_ARGS_TASK, task)
            }
        }
    }
}