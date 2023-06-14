package com.template.todoapp.ui.task_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.template.todoapp.R
import com.template.todoapp.databinding.FragmentTaskBinding
import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.ui.main_screen.MainFragment
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
            viewModel.setTodo((getParcelable(KEY_ARGS_TASK) as TodoItem?))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todoItemState.collectLatest { todoItemNull ->
                todoItemNull?.let { todoItem ->
                    changeUi(todoItem)
                }
            }

            viewModel.deadline.collectLatest {
                binding.deadlineDate.text = it.toFormatDate()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nullErrorText.collect {
                if (it) {
                    binding.editTextTask.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_error_input_task)
                    binding.editTextTask.hint = getString(R.string.hint_error_null_task)
                } else {
                    binding.editTextTask.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_normal_input_task)
                    binding.editTextTask.hint = getString(R.string.hint_edit_add_task)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.closeScreen.collect {
                if (it) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container_view, MainFragment())
                        .commit()
                }
            }
        }


        binding.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.deadlineDate.isVisible = isChecked
            binding.deadlineCalendar.isVisible = isChecked

            if (isChecked) {
                if (binding.deadlineDate.text == "") {
                    viewModel.setDeadline(binding.deadlineCalendar.date)
                }
            } else {
                viewModel.setDeadline(null)
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteTodo()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.saveTask.setOnClickListener {
            val importance = when (binding.importanceSpinner.selectedItemPosition) {
                0 -> Importance.REGULAR
                1 -> Importance.LOW
                2 -> Importance.URGENT
                else -> {
                    throw RuntimeException()
                }
            }
            viewModel.saveTask(importance, Calendar.getInstance().timeInMillis)
        }

        binding.closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.deadlineCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.setDeadline(GregorianCalendar(year, month, dayOfMonth).timeInMillis)
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
            viewModel.setTaskText(todoItem.text)
            when (todoItem.importance) {
                Importance.LOW -> binding.importanceSpinner.setSelection(0)
                Importance.REGULAR -> binding.importanceSpinner.setSelection(1)
                Importance.URGENT -> binding.importanceSpinner.setSelection(2)
            }

            deadlineDate.isVisible = todoItem.deadline != null
            deadlineDate.text = todoItem.deadline.toFormatDate()
            deadlineSwitch.isChecked = todoItem.deadline != null

            if (todoItem.deadline != null) {
                deadlineCalendar.isVisible = true
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