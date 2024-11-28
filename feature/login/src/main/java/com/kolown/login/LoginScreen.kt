package com.kolown.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.kolown.designsystem.Primary
import com.kolown.designsystem.PrimaryUnActive
import com.kolown.designsystem.component.PorringTextField
import com.kolown.login.R.drawable
import com.kolown.login.R.string
import com.kolown.login.component.LoginButtonGroup
import com.kolown.login.component.LoginTopAppBar
import com.kolown.login.component.LogoItem
import com.kolown.login.util.LoginButton.PainterIconButton
import com.kolown.login.util.LoginButton.VectorIconButton
import com.kolown.login.util.LoginPlatform
import com.kolown.login.util.getCredential
import com.kolown.model.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    updateLoginState: () -> Unit,
    popBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    navigateToJoin: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    padding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current
    val isEmailLogin by loginViewModel.isEmailLogin.collectAsStateWithLifecycle()
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    var isLoginProgress by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Idle -> {
                isLoginProgress = false
            }

            is UiState.Success -> {
                popBackStack()
                updateLoginState()
                onShowSnackBar((loginState as UiState.Success<String>).data)
            }

            is UiState.Failure -> {
                val error = (loginState as UiState.Failure).error

                when (error) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        when (error.errorCode) {
                            "ERROR_INVALID_EMAIL" -> onShowSnackBar("이메일 형식으로 입력해주세요")
                            "ERROR_INVALID_CREDENTIAL" -> onShowSnackBar("이메일 혹은 비밀번호를 확인해주세요")
                            else -> onShowSnackBar("이메일 혹은 비밀번호를 확인해주세요")
                        }
                    }

                    is FirebaseNetworkException -> {
                        onShowSnackBar("인터넷 연결을 확인해주세요")
                    }
                }

                (loginState as UiState.Failure).error.let {
                    Log.e("LoginScreen", "fatal: ${it}")
                }
                isLoginProgress = false
            }

            is UiState.Loading -> {
                isLoginProgress = true
            }
        }
    }

    LoginScreen(
        isEmailLogin = isEmailLogin,
        isLoginProgress = isLoginProgress,
        navigateToJoin = navigateToJoin,
        onClickGoogleLogin = {
            CoroutineScope(Dispatchers.Main).launch {
                getCredential(LoginPlatform.Google, context).getOrNull()?.let {
                    loginViewModel.handleSignIn(it)
                }
            }
        },
        onClickEmailLogin = loginViewModel::signInWithEmailAndPassword,
        onClickEmailMode = { loginViewModel.changeEmailLogin(true) },
        cancelEmailMode = { loginViewModel.changeEmailLogin(false) },
        popBackStack = popBackStack,
        padding = padding
    )
}

@Composable
fun LoginScreen(
    isEmailLogin: Boolean = false,
    isLoginProgress: Boolean = false,
    navigateToJoin: () -> Unit = {},
    onClickEmailLogin: (String, String) -> Unit = { _, _ -> },
    onClickGoogleLogin: () -> Unit = {},
    onClickEmailMode: () -> Unit = {},
    cancelEmailMode: () -> Unit = {},
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
    ) {
        Image(
            contentScale = ContentScale.Crop,
            painter = painterResource(drawable.bg_login),
            contentDescription = null
        )

        LoginContent(
            isEmailLogin = isEmailLogin,
            isLoginProgress = isLoginProgress,
            navigateToJoin = navigateToJoin,
            onClickGoogleLogin = onClickGoogleLogin,
            onClickEmailLogin = onClickEmailLogin,
            onClickEmailMode = onClickEmailMode
        )

        LoginTopAppBar(
            isEmailLogin = isEmailLogin,
            cancelEmailLogin = cancelEmailMode,
            popBackStack = popBackStack
        )
    }
}

@Composable
fun LoginContent(
    isEmailLogin: Boolean = false,
    isLoginProgress: Boolean = false,
    navigateToJoin: () -> Unit = {},
    onClickGoogleLogin: () -> Unit = {},
    onClickEmailLogin: (String, String) -> Unit = { _, _ -> },
    onClickEmailMode: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoItem()

        Spacer(modifier = Modifier.height(36.dp))

        if (isLoginProgress) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = Primary
            )
        } else {
            Box {
                LoginButtonGroup(
                    PainterIconButton(
                        icon = drawable.logo_google,
                        text = stringResource(string.start_with_google),
                        onClick = onClickGoogleLogin
                    ), VectorIconButton(
                        icon = Icons.Default.Email, text = "이메일로 로그인", onClick = onClickEmailMode
                    ), visible = isEmailLogin.not()
                )

                EmailLoginContent(
                    navigateToJoin = navigateToJoin,
                    onClickEmailLogin = onClickEmailLogin,
                    isEmailLogin = isEmailLogin,
                )
            }
        }

    }
}

@Composable
fun EmailLoginContent(
    isEmailLogin: Boolean = true,
    onClickEmailLogin: (String, String) -> Unit = { _, _ -> },
    navigateToJoin: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isEmailLogin,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val focusManager = LocalFocusManager.current
            var idText by rememberSaveable { mutableStateOf("") }
            var pwText by rememberSaveable { mutableStateOf("") }
            val (idField, pwField) = FocusRequester.createRefs()
            var isLoginEnable by remember { mutableStateOf(false) }

            PorringTextField(
                value = idText,
                onValueChange = {
                    idText = it
                    isLoginEnable = if (idText.isNotEmpty() && pwText.isNotEmpty()) {
                        true
                    } else {
                        false
                    }
                },
                hint = "이메일을 입력해주세요",
                label = "Email",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = {
                    pwField.requestFocus()
                }),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                    .focusRequester(idField)
            )

            PorringTextField(
                value = pwText,
                onValueChange = {
                    pwText = it
                    isLoginEnable = if (idText.isNotEmpty() && pwText.isNotEmpty()) {
                        true
                    } else {
                        false
                    }
                },
                hint = "비밀번호를 입력해주세요",
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() },
                    onPrevious = { idField.requestFocus() }),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                    .focusRequester(pwField)
            )

            Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                enabled = isLoginEnable,
                onClick = {
                    onClickEmailLogin(idText, pwText)
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
                Text(text = "로그인")
            }

            TextButton(
                onClick = { navigateToJoin() },
            ) {
                Text(
                    text = "회원가입", color = Primary, style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLoginScreen() {
    LoginScreen()
}

@Composable
@Preview(showBackground = true)
fun PreviewEmailLoginContent() {
    EmailLoginContent()
}
