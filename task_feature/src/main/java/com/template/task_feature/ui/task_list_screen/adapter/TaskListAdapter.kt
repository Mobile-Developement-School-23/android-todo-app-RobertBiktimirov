package com.template.task_feature.ui.task_list_screen.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.template.common.utli.timestampToFormattedDate
import com.template.resourses_module.R
import com.template.task_feature.databinding.ItemTaskListBinding
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.domain.entity.TodoItem

class TaskListAdapter(
    private val onChooseClickListener: ((TodoItem) -> Unit),
    private val onInfoClickListener: ((TodoItem) -> Unit)
) : ListAdapter<TodoItem, TaskListAdapter.TaskViewHolder>(TaskDiffUtil()) {

    private val _mapTodoItem = mutableMapOf<Int, TodoItem>()
    val mapTodoItem get() = _mapTodoItem.toMap()

    private val mapHolders = mutableMapOf<Int, TaskViewHolder>()


    inner class TaskViewHolder(val binding: ItemTaskListBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todoItem: TodoItem) {

            mapHolders[adapterPosition] = this

            with(binding) {

                titleTask.text = todoItem.text
                dataTask.text = todoItem.deadline.timestampToFormattedDate()
                dataTask.isVisible = todoItem.deadline != null
                isChooseBoxTask.isChecked = todoItem.isCompleted
                setupDisplayTaskText(this@TaskViewHolder, false, todoItem)

//                binding.isChooseBoxTask.setOnCheckedChangeListener { _, isChecked ->
//                    onChooseClickListener(todoItem.copy(isCompleted = isChecked))
//                    setupDisplayTaskText(this@TaskViewHolder, isChecked, todoItem)
//                    Log.d("okhttp.OkHttpClient", "from adapter call update")
//                }

                binding.isChooseBoxTask.setOnClickListener {
                    Log.d("isChooseBoxTaskTest", binding.isChooseBoxTask.isChecked.toString())
                }

                when (todoItem.importance) {
                    Importance.URGENT -> {
                        isChooseBoxTask.buttonTintList = ColorStateList.valueOf(
                            context.resources.getColor(
                                R.color.color_light_red,
                                null
                            )
                        )
                        typeTask.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.urgent,
                                null
                            )
                        )

                    }
                    Importance.LOW -> {
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
                        typeTask.setImageDrawable(null)
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

        holder.binding.bodyTask.setOnClickListener {
            onInfoClickListener(getItem(holder.adapterPosition))
        }

        return holder
    }

    private fun setupDisplayTaskText(
        holder: TaskViewHolder,
        isChecked: Boolean,
        todoItem: TodoItem
    ) {
        with(holder.binding) {
            if (isChecked || todoItem.isCompleted) {
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
                titleTask.text = todoItem.text
            }
        }
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
