package com.template.task_feature.ui.utlis

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.template.resourses_module.R

fun View.showSnackbarNoInternet(job: (() -> Unit)) {

    Snackbar.make(
        this,
        this.context.getString(R.string.no_internet_by_save),
        Snackbar.LENGTH_INDEFINITE
    ).apply {
        setAction("Ok") {
            job()
        }
    }.show()

}