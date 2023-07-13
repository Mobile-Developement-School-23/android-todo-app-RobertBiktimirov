package com.template.task_feature.ui.task_screen.compose_views

import android.widget.CalendarView
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.template.common.utli.timestampToFormattedDate
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.ui.task_screen.compose_views.ui_theme.ExtendedTheme.colors
import com.template.task_feature.ui.task_screen.compose_views.ui_theme.TodoAppTheme
import com.template.task_feature.ui.task_screen.screen_state.Action
import com.template.task_feature.ui.task_screen.screen_state.CloseImportanceDio
import com.template.task_feature.ui.task_screen.screen_state.CloseScreen
import com.template.task_feature.ui.task_screen.screen_state.DeadlineChange
import com.template.task_feature.ui.task_screen.screen_state.Delete
import com.template.task_feature.ui.task_screen.screen_state.ImportanceChange
import com.template.task_feature.ui.task_screen.screen_state.ImportanceClick
import com.template.task_feature.ui.task_screen.screen_state.SaveScreen
import com.template.task_feature.ui.task_screen.screen_state.TaskScreenState
import com.template.task_feature.ui.task_screen.screen_state.TextChange
import com.template.task_feature.ui.utlis.toScreenString
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.GregorianCalendar

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
                tint = colors.labelPrimary
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
                color = colors.backBlueText
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskText(text: String, isNullText: Boolean, setText: ((String) -> Unit)) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 16.dp, end = 16.dp)
    ) {

        TextField(
            value = text,
            onValueChange = { changeValue -> setText(changeValue) },
            placeholder = {
                Text(text = if (!isNullText) "Что надо сделать..." else "Обязательно поле")
            },
            textStyle = TextStyle(),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .heightIn(min = 120.dp)
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                .background(
                    color = colors.backSecondary,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = if (!isNullText) colors.backSecondary else colors.backRedColor
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
        color = colors.supportSeparator

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
            color = colors.labelPrimary,
            fontSize = 16.sp
        )

        Text(
            text = importance.toScreenString(),
            color = when (importance) {
                Importance.URGENT -> colors.backRedColor
                else -> colors.labelPrimary
            },
            modifier = Modifier.padding(0.dp)
        )
    }
}


@Composable
fun DeadlineView(deadline: Long?, setDeadline: ((Long?) -> Unit)) {

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Сделать до",
                    color = colors.labelPrimary,
                    fontSize = 16.sp
                )
                if (deadline != null) {
                    Text(
                        text = deadline.timestampToFormattedDate(),
                        color = colors.backBlueText,
                        fontSize = 14.sp
                    )
                }
            }

            Switch(
                checked = deadline != null,
                onCheckedChange = { changed ->
                    if (!changed) {
                        setDeadline(null)
                    } else {
                        setDeadline(getTimeInMillis())
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
            )
        }
        AnimatedVisibility(
            visible = deadline != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AndroidView(
                {
                    CalendarView(it)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                update = { calendar ->
                    calendar.date = deadline ?: getTimeInMillis()
                    calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
                        setDeadline(GregorianCalendar(year, month, dayOfMonth).timeInMillis)
                    }
                }
            )
        }
    }
}

private fun getTimeInMillis() = Calendar.getInstance().timeInMillis


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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskScreenWithBottomDio(
    onAction: ((Action) -> Unit),
    state: MutableStateFlow<TaskScreenState>
) {
    val screenState = state.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(if (screenState.value.isImportanceDioOpen) BottomSheetValue.Expanded else BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        backgroundColor = colors.backPrimary,
        sheetContent = {

            Box(
                Modifier
                    .fillMaxWidth()
                    .background(colors.labelSecondary)
                    .height(150.dp)
            ) {
                Column(
                    content = {
                        Spacer(modifier = Modifier.padding(16.dp))
                        Text(
                            text = "Выбор важности задачи",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = colors.backPrimary
                        )

                    }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Box(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth(0.9f)
                        .align(Alignment.BottomCenter)
                        .background(colors.backPrimary, shape = RoundedCornerShape(60.dp))
                ) {

                    TextButton(onClick = {
                        onAction(ImportanceChange(Importance.REGULAR))
                    }, modifier = Modifier.align(Alignment.CenterStart)) {
                        Text(text = "Нет", color = colors.labelSecondary)
                    }

                    TextButton(onClick = {
                        onAction(ImportanceChange(Importance.LOW))
                    }, modifier = Modifier.align(Alignment.Center)) {
                        Text(text = "Низкий", color = colors.labelSecondary)
                    }

                    TextButton(onClick = {
                        onAction(ImportanceChange(Importance.URGENT))
                    }, modifier = Modifier.align(Alignment.CenterEnd)) {
                        Text(text = "!! Высокий", color = colors.labelSecondary)
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
    ) {

        TodoAppTheme {
            TaskScreen(onAction = onAction, screenState = state)
        }

    }
}


@Composable
fun TaskScreen(
    onAction: ((Action) -> Unit),
    screenState: MutableStateFlow<TaskScreenState>,
) {

    val state = screenState.collectAsState()

    TodoAppTheme {

        if (state.value.isLoading) {
            CircularProgressIndicator()
        }

        Box(modifier = Modifier.fillMaxSize()) {

            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Column(
                Modifier.verticalScroll(rememberScrollState())
            ) {

                TaskToolbar({
                    onAction(CloseScreen)
                }, {
                    onAction(SaveScreen)
                })

                EditTaskText(state.value.text, state.value.nullTextChange) {
                    onAction(TextChange(it))
                }

                ImportanceTask(importance = state.value.importance) {
                    onAction(ImportanceClick)
                }
                SampleLine()

                DeadlineView(deadline = state.value.deadline) {
                    onAction(DeadlineChange(it))
                }

                SampleLine()

                DeleteButton {
                    onAction(Delete)
                }
            }

            if (state.value.isImportanceDioOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                        .clickable {
                            onAction(CloseImportanceDio)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {

    TodoAppTheme {
        TaskScreenWithBottomDio(
            onAction = {},
            state = MutableStateFlow(TaskScreenState())
        )
    }
}
