package com.template.todoapp.ui.task_screen.spinner_adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.template.resourses_module.R

class SpinnerAdapter(context: Context, objects: Array<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if (position == count - 1) {
            val color = ContextCompat.getColor(context, R.color.red)
            (view as TextView).setTextColor(color)
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }


}