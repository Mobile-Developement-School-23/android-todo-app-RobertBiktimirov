package com.template.task_feature.ui.task_navigation

interface TaskNavigation {

    fun onBack()
    fun goTaskFragment(todoId: String?)

    fun goSettingFragment()

}