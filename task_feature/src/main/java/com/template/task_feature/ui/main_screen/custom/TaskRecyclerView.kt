package com.template.task_feature.ui.main_screen.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class TaskRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var touchListener: OnTouchListener? = null

    override fun setOnTouchListener(l: OnTouchListener?) {
        touchListener = l
    }


    override fun performClick(): Boolean {
        super.performClick()
        return touchListener?.onTouch(
            this,
            MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0f, 0f, 0)
        ) ?: false
    }
}