package com.template.task_feature.di.modules

import android.view.MotionEvent
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.resourses_module.R
import com.template.task_feature.databinding.FragmentMainBinding
import com.template.task_feature.databinding.FragmentTaskBinding
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.ui.task_list_screen.TaskListViewModel
import com.template.task_feature.ui.task_navigation.TaskNavigation
import com.template.task_feature.ui.task_screen.TaskViewModel
import com.template.task_feature.ui.utlis.showSnackbarNoInternet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressWarnings("Unused")
@ViewScope
class TaskListViewBootstrapper @Inject constructor(
    private val taskListViewController: TaskListViewController,
)