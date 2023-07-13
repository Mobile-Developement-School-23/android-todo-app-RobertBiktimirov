package com.template.task_feature.ui.task_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.common.utli.RepositoryError
import com.template.common.utli.RepositoryException
import com.template.common.utli.RepositoryResult
import com.template.common.utli.RepositorySuccess
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.usecase.DeleteTodoUseCase
import com.template.task_feature.domain.usecase.GetTodoItemUseCase
import com.template.task_feature.domain.usecase.SaveTodoItemUseCase
import com.template.task_feature.domain.usecase.UpdateTodoListUseCase
import com.template.task_feature.ui.task_screen.screen_state.Action
import com.template.task_feature.ui.task_screen.screen_state.CloseImportanceDio
import com.template.task_feature.ui.task_screen.screen_state.CloseScreen
import com.template.task_feature.ui.task_screen.screen_state.ConsentToOffline
import com.template.task_feature.ui.task_screen.screen_state.DeadlineChange
import com.template.task_feature.ui.task_screen.screen_state.Delete
import com.template.task_feature.ui.task_screen.screen_state.ImportanceChange
import com.template.task_feature.ui.task_screen.screen_state.ImportanceClick
import com.template.task_feature.ui.task_screen.screen_state.SaveScreen
import com.template.task_feature.ui.task_screen.screen_state.TaskScreenState
import com.template.task_feature.ui.task_screen.screen_state.TextChange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

internal class TaskViewModel @Inject constructor(
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val saveTodoItemUseCase: SaveTodoItemUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase,
    private val getTodoItemUseCase: GetTodoItemUseCase
) : ViewModel() {

    private val _todoItemState = MutableStateFlow<TodoItem?>(null)
    private val _closeScreen = MutableStateFlow(false)
    val closeScreen get() = _closeScreen.asStateFlow()
    private val _noInternet = MutableStateFlow(false)
    val noInternet get() = _noInternet.asStateFlow()

    private val _importanceClick: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val importanceClick get() = _importanceClick.asStateFlow()

    val state: MutableStateFlow<TaskScreenState> = MutableStateFlow(TaskScreenState())

    fun onAction(action: Action) {

        when (action) {
            is CloseScreen -> {
                _closeScreen.tryEmit(true)
            }

            is SaveScreen -> {
                state.update {
                    it.copy(isLoading = true)
                }
                saveTask(state.value)
            }

            is TextChange -> {
                state.update {
                    it.copy(text = action.text, nullTextChange = false)
                }
            }

            is DeadlineChange -> {
                state.update {
                    it.copy(deadline = action.deadline)
                }
            }

            is ImportanceChange -> {
                state.update {
                    it.copy(importance = action.importance, isImportanceDioOpen = false)
                }
            }

            is ConsentToOffline -> {
                onAction(CloseScreen)
            }

            Delete -> {
                deleteTodo()
            }

            ImportanceClick -> {
                state.update {
                    it.copy(isImportanceDioOpen = !it.isImportanceDioOpen)
                }
            }

            CloseImportanceDio -> {
                state.update {
                    it.copy(isImportanceDioOpen = false)
                }
            }
        }
    }

    fun getTodoItem(id: String) {
        viewModelScope.launch {

            when (val todoItem = getTodoItemUseCase.invoke(id)) {
                is RepositoryError -> {
                    state.update {
                        it.copy(error = true, isLoading = false)
                    }
                }

                is RepositoryException -> {
                    state.update {
                        it.copy(error = true, isLoading = false)
                    }
                }

                is RepositorySuccess -> {
                    _todoItemState.emit(todoItem.data)
                    state.update {
                        it.copy(
                            id = todoItem.data.id,
                            error = false,
                            isLoading = false,
                            text = todoItem.data.text,
                            deadline = todoItem.data.deadline,
                            importance = todoItem.data.importance,
                            isAddScreen = false,
                            isCompleted = todoItem.data.isCompleted,
                            dateOfCreating = todoItem.data.dateOfCreating,
                            dateOnChanged = todoItem.data.dateOfEditing
                        )
                    }
                }
            }

        }
    }

    private fun saveTask(state: TaskScreenState) {
        viewModelScope.launch {
            saveOrUpdateTask(state)
        }
    }

    private fun deleteTodo() {
        viewModelScope.launch {

            with(state.value) {
                if (id == "") {
                    onAction(CloseScreen)
                    return@launch
                }
                handleUpdateOrSaveResult(
                    deleteTodoUseCase(
                        TodoItem(
                            id,
                            text,
                            importance,
                            deadline,
                            isCompleted,
                            color = null,
                            dateOfCreating,
                            dateOnChanged
                        )
                    )
                )
            }
        }
    }

    private suspend fun saveOrUpdateTask(stateScreen: TaskScreenState) {

        if (stateScreen.text != "") {

            if (stateScreen.isAddScreen) {

                handleUpdateOrSaveResult(
                    saveTodoItemUseCase(
                        TodoItem(
                            id = generateTodoId(),
                            text = stateScreen.text,
                            importance = stateScreen.importance,
                            deadline = stateScreen.deadline,
                            isCompleted = false,
                            color = null,
                            Calendar.getInstance().timeInMillis,
                            null,
                        )
                    )
                )
            } else {
                handleUpdateOrSaveResult(
                    updateTodoListUseCase(
                        TodoItem(
                            id = stateScreen.id,
                            text = stateScreen.text,
                            importance = stateScreen.importance,
                            deadline = stateScreen.deadline,
                            isCompleted = stateScreen.isCompleted,
                            color = null,
                            dateOfCreating = stateScreen.dateOfCreating,
                            Calendar.getInstance().timeInMillis
                        )
                    )
                )
            }
        } else {
            state.update {
                it.copy(nullTextChange = true, isLoading = false)
            }
        }
    }


    private fun handleUpdateOrSaveResult(result: RepositoryResult<TodoItem>) {

        when (result) {
            is RepositoryError -> {
                onAction(CloseScreen)
            }

            is RepositoryException -> {
                _noInternet.tryEmit(true)
                state.update {
                    it.copy(isLoading = false)
                }
                onAction(CloseScreen)
            }

            is RepositorySuccess -> {
                onAction(CloseScreen)
            }
        }
    }


    private fun generateTodoId() =
        "${Calendar.getInstance().timeInMillis} - ${kotlin.random.Random.nextInt(-1000, 1000)}"
}



