package com.template.todoapp.ui.main_screen.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.template.todoapp.R
import com.template.todoapp.databinding.ItemTaskListBinding
import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.ui.utli.toFormatDate
import java.text.SimpleDateFormat
import java.util.*

class TaskListAdapter(
    private val onChooseClickListener: ((TodoItem) -> Unit),
    private val onInfoClickListener: ((TodoItem) -> Unit)
) : ListAdapter<TodoItem, TaskListAdapter.TaskViewHolder>(TaskDiffUtil()) {

    private val _mapTodoItem = mutableMapOf<Int, TodoItem>()
    val mapTodoItem get() = _mapTodoItem.toMap()

    inner class TaskViewHolder(val binding: ItemTaskListBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem) {

            _mapTodoItem[this.adapterPosition] = todoItem

            with(binding) {
                titleTask.text = todoItem.text
                dataTask.text = todoItem.deadline.toFormatDate()
                dataTask.isVisible = todoItem.deadline != null
                isChooseBoxTask.isChecked = todoItem.flag
                when (todoItem.importance) {
                    Importance.URGENT -> {
                        isChooseBoxTask.buttonTintList = ColorStateList.valueOf(
                            context.resources.getColor(
                                R.color.red,
                                null
                            )
                        )
                        typeTask.isVisible = true
                        typeTask.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.urgent,
                                null
                            )
                        )

                    }
                    Importance.LOW -> {
                        typeTask.isVisible = true
                        typeTask.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.arrow,
                                null
                            )
                        )

                        isChooseBoxTask.buttonTintList =
                            setupNormalColorStateList()

                    }
                    Importance.REGULAR -> {
                        typeTask.isVisible = false
                        isChooseBoxTask.buttonTintList =
                            setupNormalColorStateList()
                    }
                }
            }
        }

        private fun setupNormalColorStateList() = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(
                    context,
                    R.color.green_light_theme
                ), ContextCompat.getColor(context, R.color.gray_light_light_theme)
            )
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskListBinding.inflate(inflater)
        val holder = TaskViewHolder(binding, parent.context)

        holder.binding.isChooseBoxTask.setOnCheckedChangeListener { _, isChecked ->
            onChooseClickListener(getItem(holder.adapterPosition).copy(flag = isChecked))

            with(holder.binding) {
                if (isChecked) {
                    val spannableString = SpannableString(titleTask.text)
                    spannableString.setSpan(
                        StrikethroughSpan(),
                        0,
                        spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    titleTask.text = spannableString
                    titleTask.alpha = 0.3F
                } else {
                    titleTask.alpha = 1F
                    titleTask.text = getItem(holder.adapterPosition).text
                }
            }

        }

        holder.binding.infoTask.setOnClickListener {
            onInfoClickListener(getItem(holder.adapterPosition))
        }


        return holder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TaskDiffUtil : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}
