package com.template.todoapp.ui.main_screen.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Half.toFloat
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginRight
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.template.todoapp.R
import com.template.todoapp.domain.TodoItem
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class TaskListTouchHelper(
    private val context: Context,
    private val setupTaskBySwipeImpl: SetupTaskBySwipe
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.white_delete) ?: throwErrorIcon()
    private val doneIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.check) ?: throwErrorIcon()

    private val deleteColor = ContextCompat.getColor(context, R.color.red).toDrawable()
    private val doneColor = ContextCompat.getColor(context, R.color.green_light_theme).toDrawable()

    var isSwipeInProgress: Boolean by Delegates.notNull()

    private val width = 72.dp

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        if (direction == ItemTouchHelper.LEFT) {
            setupTaskBySwipeImpl.deleteTask(position)
        } else {
            setupTaskBySwipeImpl.subscribeOnTask(position)
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isSwipeRight = dX > 0

        isSwipeInProgress = actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive


        if (isSwipeInProgress) {
            if (isSwipeRight) {
                doneColor.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + 72.dp,
                    itemView.bottom
                )
                doneColor.draw(canvas)
            } else {
                deleteColor.setBounds(
                    itemView.right - 72.dp,
                    itemView.top,
                    itemView.right + itemView.marginRight,
                    itemView.bottom
                )
                deleteColor.draw(canvas)
            }


            val iconTop = itemView.top + (itemHeight - deleteIcon.intrinsicHeight) / 2
            val iconBottom = iconTop + deleteIcon.intrinsicHeight
            val iconLeft: Int
            val iconRight: Int

            if (isSwipeRight) {
                iconLeft = itemView.left + 16.dp
                iconRight = itemView.left + 16.dp + doneIcon.intrinsicWidth
                doneIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                doneIcon.draw(canvas)
            } else {
                iconLeft = itemView.right - 16.dp - deleteIcon.intrinsicWidth
                iconRight = itemView.right - 16.dp
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(canvas)
            }
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface SetupTaskBySwipe {
        fun deleteTask(position: Int)
        fun subscribeOnTask(position: Int)
    }

    fun clearState(){
        isSwipeInProgress = false
    }

    private fun throwErrorIcon(): Nothing = throw RuntimeException("resources not found")

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), context.resources.displayMetrics
        ).roundToInt()
}