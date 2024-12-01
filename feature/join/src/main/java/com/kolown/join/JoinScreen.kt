package com.kolown.join

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.kolown.designsystem.component.PorringIconButton
import com.kolown.designsystem.component.PorringTextField
import com.kolown.designsystem.component.PorringTopAppBar
import com.kolown.designsystem.ui.theme.Error
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.model.UiState

@Composable
internal fun JoinRoute(
    onShowSnackBar: (String) -> Unit,
    popBackStack: () -> Unit,
    joinViewModel: JoinViewModel = hiltViewModel(),
    padding: PaddingValues,
) {
    val joinState by joinViewModel.joinState.collectAsStateWithLifecycle()
    var isProgress by remember { mutableStateOf(false) }

    LaunchedEffect(joinState) {
        when (joinState) {
            is UiState.Idle -> {
                isProgress = false
            }

            is UiState.Success -> {
                onShowSnackBar("회원가입 완료")
                popBackStack()
            }

            is UiState.Loading -> {
                isProgress = true
            }

            is UiState.Failure -> {
                val exception = (joinState as UiState.Failure).error

                when (exception) {
                    is FirebaseAuthUserCollisionException -> onShowSnackBar("이미 가입 된 이메일 입니다.")
                    else -> Log.e(
                        "JoinScreen",
                        "fatal: ${(joinState as UiState.Failure).error}"
                    )
                }
                isProgress = false
            }
        }
        if (joinState is UiState.Success) {
            popBackStack()
        }
    }

    JoinScreen(
        isProgress = isProgress,
        joinWithEmailAndPassword = joinViewModel::joinWithEmailAndPassword,
        popBackStack = popBackStack,
        padding = padding
    )
}

@Composable
private fun JoinScreen(
    isProgress: Boolean = false,
    joinWithEmailAndPassword: (String, String) -> Unit = { _, _ -> },
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding)
    ) {
        JoinContent(
            isProgress = isProgress, joinWithEmailAndPassword = joinWithEmailAndPassword
        )

        PorringTopAppBar(
            trailingIcon = {
                PorringIconButton(
                    icon = Icons.Default.Close,
                    onClick = popBackStack,
                    contentDescription = "뒤로가기"
                )
            }
        )
    }
}

@Composable
fun JoinContent(
    isProgress: Boolean = false,
    joinWithEmailAndPassword: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirm by remember { mutableStateOf("") }
        var emailValidation by remember { mutableStateOf(false) }
        var passwordValidation by remember { mutableStateOf(false) }
        var confirmValidation by remember { mutableStateOf(false) }
        var errorMsg by remember { mutableStateOf<List<String>>(emptyList()) }

        val focusManager = LocalFocusManager.current
        val (focus1, focus2, focus3) = FocusRequester.createRefs()

        PorringTextField(
            value = email,
            onValueChange = { string ->
                val regex = Regex(context.getString(R.string.regex_email))

                email = string
                emailValidation = regex.matches(string)
            },
            hint = stringResource(R.string.string_input_email),
            leadingIcon = Icons.Default.Email,
            label = "Email",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focus2.requestFocus() }),
            validator = { emailValidation.not() },
            onErrorChange = { isError ->
                errorMsg = if (isError) {
                    when {
                        email.isEmpty() -> errorMsg + context.getString(R.string.string_input_email)
                        else -> errorMsg + context.getString(R.string.string_invalid_email)
                    }
                } else {
                    emptyList()
                }
            },
            modifier = modifier.padding(horizontal = 32.dp)
                .focusRequester(focus1)
        )

        PorringTextField(
            value = password,
            onValueChange = { string ->
                val regex = Regex(context.getString(R.string.regex_password))

                password = string
                passwordValidation = regex.matches(string) && password.length > 7
                confirmValidation = password == confirm && confirm.isNotEmpty()
            },
            hint = stringResource(R.string.string_input_password),
            leadingIcon = Icons.Default.Lock,
            label = "Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focus3.requestFocus() }),
            validator = { passwordValidation.not() },
            onErrorChange = { isError ->
                errorMsg = if (isError) {
                    when {
                        email.isEmpty() -> errorMsg + context.getString(R.string.string_input_password)
                        confirmValidation.not() -> errorMsg + context.getString(R.string.string_invalid_password_confirm)
                        else -> errorMsg + context.getString(R.string.string_invalid_password)
                    }
                } else {
                    emptyList()
                }
            },
            modifier = modifier.padding(horizontal = 32.dp).focusRequester(focus2)
        )

        PorringTextField(
            value = confirm,
            onValueChange = {
                confirm = it
                confirmValidation = password == confirm && confirm.isNotEmpty()
            },
            hint = stringResource(R.string.string_confirm_password),
            leadingIcon = Icons.Default.CheckCircle,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            label = "Confirm",
            validator = { confirmValidation.not() },
            onErrorChange = { isError ->
                errorMsg = if (isError) {
                    when {
                        email.isEmpty() -> errorMsg + context.getString(R.string.string_confirm_password)
                        else -> errorMsg + context.getString(R.string.string_invalid_password_confirm)
                    }
                } else {
                    emptyList()
                }
            },
            modifier = modifier.padding(horizontal = 32.dp).focusRequester(focus3)
        )

        if (isProgress) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        } else {
            Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                enabled = emailValidation && passwordValidation && confirmValidation,
                onClick = {
                    focusManager.clearFocus()
                    joinWithEmailAndPassword(email, password)
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

        if (errorMsg.isNotEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                errorMsg.forEach {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        color = Error,
                        text = "- $it"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJoinScreen() {
    JoinScreen()
}
