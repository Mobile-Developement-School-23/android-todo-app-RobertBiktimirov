package com.template.task_feature.ui.task_list_screen.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.template.task_feature.R

class ItemTaskList @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val checkBox: CheckBox
    private val importance: ImageView
    private val text: TextView
    private val date: TextView
    private val info: ImageView

    init {
        inflate(context, R.layout.item_task_list, this)
        checkBox = findViewById(R.id.is_choose_box_task)
        importance = findViewById(R.id.type_task)
        text = findViewById(R.id.title_task)
        date = findViewById(R.id.data_task)
        info = findViewById(R.id.info_task)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(checkBox, widthMeasureSpec, 0, heightMeasureSpec, 0)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {




    }
}