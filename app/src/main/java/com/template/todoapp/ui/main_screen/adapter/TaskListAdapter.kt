package com.template.todoapp.ui.main_screen.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.template.todoapp.R
import com.template.todoapp.databinding.ItemTaskListBinding
import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import java.text.SimpleDateFormat
import java.util.*

class TaskListAdapter(
    private val onChooseClickListener: ((TodoItem) -> Unit),
    private val onInfoClickListener: ((TodoItem) -> Unit)
) : ListAdapter<TodoItem, TaskListAdapter.ViewHolder>(TaskDiffUtil()) {
    class ViewHolder(val binding: ItemTaskListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem, context: Context) {

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
                    }
                    Importance.REGULAR -> {
                        typeTask.isVisible = false
                    }
                }
            }
        }

        private fun Long?.toFormatDate(): String {
            val date = Date(this ?: 0)
            val format = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
            return format.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskListBinding.inflate(inflater)
        val holder = ViewHolder(binding)

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }
}

class TaskDiffUtil : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}
