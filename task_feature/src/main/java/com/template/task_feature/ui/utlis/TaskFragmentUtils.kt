package com.template.task_feature.ui.utlis

import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.template.resourses_module.R
import com.template.task_feature.domain.entity.Importance

fun Spinner.getImportanceBySelected(): Importance {
    val importance = when (selectedItemPosition) {
        0 -> Importance.REGULAR
        1 -> Importance.LOW
        2 -> Importance.URGENT
        else -> {
            throw RuntimeException()
        }
    }

    return importance
}


fun Importance.setImportance(view: Spinner) {

    when (this) {
        Importance.REGULAR -> view.setSelection(0)
        Importance.LOW -> view.setSelection(1)
        Importance.URGENT -> view.setSelection(2)
    }
}

fun EditText.setBgNullErrorText(flag: Boolean) {
    if (flag) {
        background =
            ContextCompat.getDrawable(context, R.drawable.bg_error_input_task)
        hint = context.getString(R.string.hint_error_null_task)
    } else {
        background =
            ContextCompat.getDrawable(context, R.drawable.bg_normal_input_task)
        hint = context.getString(R.string.hint_edit_add_task)
    }
}
