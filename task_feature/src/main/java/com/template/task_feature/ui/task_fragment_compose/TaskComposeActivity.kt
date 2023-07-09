package com.template.task_feature.ui.task_fragment_compose

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.template.common.utli.timestampToFormattedDate
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.ui.task_fragment_compose.ui.theme.ExtendedTheme
import com.template.task_feature.ui.task_fragment_compose.ui.theme.ExtendedTheme.colors
import com.template.task_feature.ui.task_fragment_compose.ui.theme.TodoAppTheme
import com.template.task_feature.ui.utlis.toScreenString

class TaskComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun TaskToolbar(closeClick: (() -> Unit), saveClick: (() -> Unit)) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { closeClick() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = ExtendedTheme.colors.labelPrimary
            )
        }

        TextButton(
            onClick = { saveClick() },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp, top = 16.dp)

        ) {
            Text(
                text = "Сохранить".uppercase(),
                fontSize = 16.sp,
                color = ExtendedTheme.colors.backBlueText
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskText(inputTask: MutableState<TextFieldValue>) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 16.dp, end = 16.dp)
    ) {

        TextField(
            value = inputTask.value,
            onValueChange = { changeValue -> inputTask.value = changeValue },
            placeholder = {
                Text(
                    text = "Что надо сделать..."
                )
            },
            textStyle = TextStyle(),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                .background(
                    color = ExtendedTheme.colors.backSecondary,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = ExtendedTheme.colors.backSecondary
            )
        )
    }
}

@Composable
fun SampleLine() {

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        color = ExtendedTheme.colors.supportSeparator

    )
}

@Composable
fun ImportanceTask(importance: Importance, onClick: (() -> Unit)) {

    Column(modifier = Modifier
        .background(Color.Transparent)
        .clickable {
            onClick()
        }
        .padding(start = 16.dp, top = 28.dp, end = 16.dp)) {

        Text(
            text = "Важность",
            modifier = Modifier,
            color = ExtendedTheme.colors.labelPrimary,
            fontSize = 16.sp
        )

        Text(
            text = importance.toScreenString(),
            color = when (importance) {
                Importance.URGENT -> colors.backRedColor
                else -> ExtendedTheme.colors.labelPrimary
            },
            modifier = Modifier.padding(0.dp)
        )
    }
}


@Composable
fun DeadlineView(deadline: MutableState<Long?>) {

    val isVisibleCalendar = remember { mutableStateOf(deadline.value != null) }

    Column {

        Box(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Сделать до",
                    color = ExtendedTheme.colors.labelPrimary,
                    fontSize = 16.sp
                )
                if (isVisibleCalendar.value) {
                    Text(
                        text = deadline.value.timestampToFormattedDate(),
                        color = ExtendedTheme.colors.backBlueText,
                        fontSize = 14.sp
                    )
                }
            }

            Switch(
                checked = isVisibleCalendar.value,
                onCheckedChange = { changed -> isVisibleCalendar.value = changed },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
            )
        }
        AnimatedVisibility(
            visible = isVisibleCalendar.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AndroidView(
                {
                    CalendarView(
                        ContextThemeWrapper(
                            it,
                            com.template.resourses_module.R.style.CalendarStyle
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                update = { calendar ->
                    calendar.date = deadline.value ?: 0
                    calendar.setOnDateChangeListener { calendarView, _, _, _ ->
                        deadline.value = calendarView.date
                    }
                }
            )
        }
    }
}


@Composable
fun DeleteButton(deleteClick: (() -> Unit)) {

    Button(onClick = { deleteClick() }, colors = ButtonDefaults.buttonColors(Color.Transparent)) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = colors.backRedColor
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

        Text(text = "Удалить", color = colors.backRedColor, fontSize = 16.sp)
    }

}


//@Preview(showBackground = true)
//@Composable
//fun TaskToolbarPreview() {
//    TodoAppTheme {
//        TaskToolbar({}, {})
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun EditTaskTextPreview() {
//
//    TodoAppTheme {
//        EditTaskText(remember { mutableStateOf(TextFieldValue()) })
//    }
//}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {

    val inputTask: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val deadline: MutableState<Long?> = remember { mutableStateOf(2389749082378034) }
    val hardcoreImportance = Importance.URGENT



    TodoAppTheme() {
        Column(
            Modifier
                .fillMaxHeight()
                .background(colors.backPrimary)
                .verticalScroll(rememberScrollState())
        ) {
            TaskToolbar({

            }, {

            })
            EditTaskText(inputTask)
            ImportanceTask(importance = hardcoreImportance) {

            }
            SampleLine()

            DeadlineView(deadline = deadline)

            SampleLine()

            DeleteButton {

            }
        }
    }
}

