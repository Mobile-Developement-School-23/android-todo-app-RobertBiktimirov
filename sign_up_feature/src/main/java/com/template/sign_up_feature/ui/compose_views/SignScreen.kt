package com.template.sign_up_feature.ui.compose_views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.resourses_module.compose_theme.ui_theme.ExtendedTheme
import com.template.resourses_module.compose_theme.ui_theme.TodoAppTheme
import com.template.sign_up_feature.ui.setup_screen.Action
import com.template.sign_up_feature.ui.setup_screen.ClickSignUpButton
import com.template.sign_up_feature.ui.setup_screen.SignStateScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun SignScreen(
    vdState: StateFlow<SignStateScreen>,
    onAction: ((Action) -> Unit)
) {

    val screenState = vdState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            LogoView()
            AppName()
            SignInButton(
                Modifier.fillMaxWidth().weight(1f),
                screenState.value.isError
            ) { onAction(ClickSignUpButton) }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun SignScreenPreview() {
    TodoAppTheme(darkTheme = false) {
        SignScreen(MutableStateFlow(SignStateScreen())) {

        }
    }
}


@Composable
fun LogoView() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 150.dp)
    ) {
        Image(
            painter = painterResource(id = com.template.resourses_module.R.drawable.notebook__1_),
            contentDescription = "application icon",
            modifier = Modifier.align(Alignment.Center)
        )

    }
}

@Composable
fun SignInButton(
    modifier: Modifier,
    isError: Boolean,
    onClick: (() -> Unit)
) {

    Box(modifier) {

        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { onClick() },
            border = BorderStroke(
                2.dp,
                if (isError) Color.Red else ExtendedTheme.colors.yellow
            ),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ExtendedTheme.colors.backBlueText)
        ) {

            Text(
                text = "Войти с помощью Yandex",
                color = ExtendedTheme.colors.labelSecondary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
            )
        }

        if (isError) {
            Text(
                text = "Не удалось зарегистрироваться",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                color = Color.Red
            )
        }


    }

}

@Composable
fun AppName() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Text(
            text = "Приветствую в TodoApp",
            color = ExtendedTheme.colors.labelPrimary,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = typography.headlineMedium,
            modifier = Modifier
                .padding(top = 15.dp, start = 30.dp, end = 30.dp)
                .align(Alignment.Center),
        )
    }


}
