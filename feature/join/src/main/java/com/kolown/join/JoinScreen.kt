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
import com.kolown.navigation.Route

@Composable
internal fun JoinRoute(
    onShowSnackBar: (String) -> Unit,
    popBackStack: (Route) -> Unit,
    joinViewModel: JoinViewModel = hiltViewModel(),
    padding: PaddingValues,
) {
    val joinState by joinViewModel.joinState.collectAsStateWithLifecycle()
    var isProgress by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(joinState) {
        when (joinState) {
            is UiState.Idle -> {
                isProgress = false
            }

            is UiState.Success -> {
                onShowSnackBar(context.getString(R.string.string_complete_signup))
                popBackStack(Route.Login)
            }

            is UiState.Loading -> {
                isProgress = true
            }

            is UiState.Failure -> {
                val exception = (joinState as UiState.Failure).error

                when (exception) {
                    is FirebaseAuthUserCollisionException -> onShowSnackBar(context.getString(R.string.string_already_exist))
                    else -> Log.e(
                        "JoinScreen",
                        "fatal: ${(joinState as UiState.Failure).error}"
                    )
                }
                isProgress = false
            }
        }
        if (joinState is UiState.Success) {
            popBackStack(Route.Login)
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
    popBackStack: (Route) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        JoinContent(
            isProgress = isProgress, joinWithEmailAndPassword = joinWithEmailAndPassword
        )

        PorringTopAppBar(
            trailingIcon = {
                PorringIconButton(
                    icon = Icons.Default.Close,
                    onClick = { popBackStack(Route.Login) },
                    contentDescription = stringResource(R.string.string_go_back)
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
            modifier = modifier
                .padding(horizontal = 32.dp)
                .focusRequester(focus1)
        )

        PorringTextField(
            value = password,
            onValueChange = { string ->
                val regex = Regex(context.getString(R.string.regex_password))

                password = string
                passwordValidation = regex.matches(string) && password.length > 7
                confirmValidation = password == confirm
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
                    val errorList = mutableListOf<String>()

                    if (email.isEmpty()) {
                        errorList += context.getString(R.string.string_input_password)
                    } else {
                        errorList += context.getString(R.string.string_invalid_password)
                    }

                    if (confirmValidation.not()) {
                        errorList += context.getString(R.string.string_invalid_password_confirm)
                    }

                    errorList.toList()
                } else {
                    emptyList()
                }
            },
            modifier = modifier
                .padding(horizontal = 32.dp)
                .focusRequester(focus2)
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
            modifier = modifier
                .padding(horizontal = 32.dp)
                .focusRequester(focus3)
        )

        if (isProgress) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        } else {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
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
                Text(text = stringResource(R.string.string_let_join))
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
