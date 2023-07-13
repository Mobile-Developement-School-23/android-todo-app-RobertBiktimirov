package com.template.task_feature.ui.task_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.template.task_feature.databinding.FragmentTaskBinding
import com.template.task_feature.di.TaskComponentViewModel
import com.template.task_feature.di.modules.viewmodels.ViewModelFactory
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.compose_views.TaskScreen
import com.template.task_feature.ui.task_screen.compose_views.TaskScreenWithBottomDio
import com.template.task_feature.ui.task_screen.compose_views.ui_theme.TodoAppTheme
import kotlinx.coroutines.launch
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

        return inflater.inflate(com.template.task_feature.R.layout.fragment_task, container, false)
            .apply {
                findViewById<ComposeView>(com.template.task_feature.R.id.task_compose_screen).setContent {
                    TodoAppTheme {
                        TaskScreenWithBottomDio(viewModel::onAction, viewModel::state.get())
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArguments()


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.closeScreen.collect {
                if (it) {
                    navigation?.onBack()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.importanceClick.collect {
                if (it) {
                    Toast.makeText(requireContext(), "importanceClick", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun parseArguments() {
        arguments?.takeIf { it.containsKey(KEY_ARGS_TASK) }?.apply {
            getString(KEY_ARGS_TASK)?.let { viewModel.getTodoItem(it) }
        }
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