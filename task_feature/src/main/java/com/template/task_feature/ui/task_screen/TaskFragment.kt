package com.template.task_feature.ui.task_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.template.common.utli.toFormatDate
import com.template.resourses_module.R
import com.template.task_feature.databinding.FragmentTaskBinding
import com.template.task_feature.di.TaskComponentViewModel
import com.template.task_feature.di.modules.viewmodels.ViewModelFactory
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.todoapp.ui.task_screen.TaskViewModel
import com.template.todoapp.ui.task_screen.spinner_adapter.SpinnerAdapter
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]
    }

    private var navigation: TaskNavigation? = null

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
        _binding = FragmentTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.deadlineCalendar.isVisible = false

        initSpinner()

        arguments?.takeIf { it.containsKey(KEY_ARGS_TASK) }?.apply {
            getString(KEY_ARGS_TASK)?.let { viewModel.getTodoItem(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingStatus.collect {
                binding.loadingView.isVisible = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todoItemState.collect { todoItemNull ->
                todoItemNull?.let { todoItem ->
                    changeUi(todoItem)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deadline.collect {
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
                    hideKeyboard()
                    navigation?.onBack()
                }
            }
        }


        binding.deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.deadlineDate.isVisible = isChecked
            binding.deadlineCalendar.isVisible = isChecked

            if (isChecked) {
                if (viewModel.deadline.value == null) {
                    viewModel.setDeadline(binding.deadlineCalendar.date)
                }
            } else {
                viewModel.setDeadline(null)
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteTodo()
            navigation?.onBack()
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
            viewModel.closeTheScreen()
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
                Importance.REGULAR -> binding.importanceSpinner.setSelection(0)
                Importance.LOW -> binding.importanceSpinner.setSelection(1)
                Importance.URGENT -> binding.importanceSpinner.setSelection(2)
            }

            deadlineDate.isVisible = todoItem.deadline != null
            deadlineSwitch.isChecked = todoItem.deadline != null
            viewModel.setDeadline(todoItem.deadline)
            if (todoItem.deadline != null) {
                deadlineCalendar.isVisible = true
                deadlineCalendar.date = todoItem.deadline
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            binding.editTextTask.windowToken,
            0
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    override fun onDetach() {
        navigation = null
        super.onDetach()
    }

    companion object {

        private const val KEY_ARGS_TASK = "args"

        fun getNewInstance(taskId: String?): TaskFragment = TaskFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_ARGS_TASK, taskId)
            }
        }
    }
}