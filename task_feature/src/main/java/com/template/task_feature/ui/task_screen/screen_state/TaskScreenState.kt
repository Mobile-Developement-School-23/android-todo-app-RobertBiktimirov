package com.template.task_feature.ui.task_screen.screen_state

import com.template.task_feature.domain.entity.Importance

sealed interface Action

sealed interface Ui : Action

object CloseScreen : Ui
object SaveScreen : Ui
class TextChange(val text: String) : Ui
class DeadlineChange(val deadline: Long?) : Ui
class ImportanceChange(val importance: Importance) : Ui
object ImportanceClick: Ui
object CloseImportanceDio: Ui

object Delete : Ui

object ConsentToOffline : Ui


data class TaskScreenState(
    var id: String = "",
    var text: String = "",
    var deadline: Long? = null,
    var importance: Importance = Importance.REGULAR,
    var nullTextChange: Boolean = false,
    var error: Boolean = false,
    var isLoading: Boolean = false,
    var isAddScreen: Boolean = true,
    var isCompleted: Boolean = false,
    var dateOfCreating: Long = 0L,
    var dateOnChanged: Long? = null,
    var isNotInternet: Boolean = false,
    var isImportanceDioOpen: Boolean = false
)