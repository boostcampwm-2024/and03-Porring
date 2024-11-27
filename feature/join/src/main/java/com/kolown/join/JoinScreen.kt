package com.kolown.join

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kolown.designsystem.Primary
import com.kolown.designsystem.PrimaryUnActive
import com.kolown.designsystem.component.PorringTextField
import com.kolown.join.component.JoinTopAppBar

@Composable
fun JoinRoute(
    popBackStack: () -> Unit,
    padding: PaddingValues,
) {
    JoinScreen(
        popBackStack = popBackStack, padding = padding
    )
}

@Composable
fun JoinScreen(
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding)
    ) {
        Text(text = "join screen")
        JoinTopAppBar(
            popBackStack = popBackStack
        )

        JoinContent(
        )
    }
}

@Composable
fun JoinContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var id by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirm by remember { mutableStateOf("") }
        var idValidation by remember { mutableStateOf(false) }
        var passwordValidation by remember { mutableStateOf(false) }
        var confirmValidation by remember { mutableStateOf(false) }

        val focusManager = LocalFocusManager.current
        val (focus1, focus2, focus3) = FocusRequester.createRefs()

        PorringTextField(
            value = id,
            onValueChange = { string ->
                val regex = Regex("^(?!.*\\.\\.)[a-z0-9.]+@\\w+\\.[A-Za-z]{2,3}$")

                id = string
                idValidation = regex.matches(string)
            },
            hint = "이메일을 입력해주세요",
            leadingIcon = Icons.Default.Email,
            label = "Email",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focus2.requestFocus() }),
            validator = { idValidation.not() },
            modifier = modifier.padding(horizontal = 32.dp).focusRequester(focus1)
        )

        PorringTextField(
            value = password,
            onValueChange = { string ->
                val regex =
                    Regex("^(?!.* )(?=.*[!\"#\$%&'()*+,\\-./:;<=>?@\\[₩\\]^_`{|}~])[a-zA-Z0-9!\"#\$%&'()*+,\\-./:;<=>?@\\[₩\\]^_`{|}~]+$")

                password = string
                passwordValidation = regex.matches(string) && password.length > 7
            },
            hint = "비밀번호를 입력해주세요",
            leadingIcon = Icons.Default.Lock,
            label = "Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focus3.requestFocus() }),
            validator = { passwordValidation.not() },
            modifier = modifier.padding(horizontal = 32.dp).focusRequester(focus2)
        )

        PorringTextField(
            value = confirm,
            onValueChange = {
                confirm = it
                confirmValidation = password == confirm && confirm.isNotEmpty()
            },
            hint = "비밀번호를 확인해주세요",
            leadingIcon = Icons.Default.CheckCircle,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            label = "Confirm",
            validator = { confirmValidation.not() },
            modifier = modifier.padding(horizontal = 32.dp).focusRequester(focus3)
        )

        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            enabled = idValidation && passwordValidation && confirmValidation,
            onClick = {
                focusManager.clearFocus()
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonColors(
                containerColor = Primary,
                contentColor = Color.White,
                disabledContainerColor = PrimaryUnActive,
                disabledContentColor = Color.White
            )
        ) {
            Text(text = "가입하기")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJoinScreen() {
    JoinScreen()
}