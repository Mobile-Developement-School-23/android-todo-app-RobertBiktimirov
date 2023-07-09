package com.template.task_feature.ui.task_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.template.common.utli.timestampToFormattedDate
import com.template.resourses_module.R
import com.template.task_feature.databinding.FragmentTaskBinding
import com.template.task_feature.di.TaskComponentViewModel
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.utlis.getImportanceBySelected
import com.template.task_feature.ui.utlis.setBgNullErrorText
import com.template.task_feature.ui.utlis.setImportance
import com.template.task_feature.ui.utlis.showSnackbarNoInternet
import com.template.todoapp.ui.task_screen.spinner_adapter.SpinnerAdapter
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
                binding.deadlineDate.text = it.timestampToFormattedDate()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nullErrorText.collect {
                binding.editTextTask.setBgNullErrorText(it)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noInternet.collect {
                if (it) {
                    binding.root.showSnackbarNoInternet {
                        viewModel.setNoInternet(false)
                    }
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
        }

        binding.saveTask.setOnClickListener {
            viewModel.saveTask(
                binding.importanceSpinner.getImportanceBySelected(),
                Calendar.getInstance().timeInMillis
            )
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
            todoItem.importance.setImportance(binding.importanceSpinner)
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
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
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