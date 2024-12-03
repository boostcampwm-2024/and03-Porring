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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.kolown.designsystem.R
import com.kolown.designsystem.component.PorringIconButton
import com.kolown.designsystem.component.PorringTextField
import com.kolown.designsystem.component.PorringTopAppBar
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.login.R.drawable
import com.kolown.login.R.string
import com.kolown.login.component.LoginButtonGroup
import com.kolown.login.component.LogoItem
import com.kolown.login.util.LoginButton.PainterIconButton
import com.kolown.login.util.LoginButton.VectorIconButton
import com.kolown.login.util.LoginPlatform
import com.kolown.login.util.getCredential
import com.kolown.model.UiState
import kotlinx.coroutines.launch

@Composable
internal fun LoginRoute(
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
    val latestEmail by loginViewModel.latestEmail.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        loginViewModel.getLatestUserEmail()
    }

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
                            "ERROR_INVALID_EMAIL" -> onShowSnackBar(context.getString(string.string_need_email_form))
                            "ERROR_INVALID_CREDENTIAL" -> onShowSnackBar(context.getString(string.string_need_to_check_email_or_pw))
                            else -> onShowSnackBar(context.getString(string.string_check_email_pw))
                        }
                    }

                    is FirebaseNetworkException -> {
                        onShowSnackBar(context.getString(string.string_check_network))
                    }
                }

                isLoginProgress = false
            }

            is UiState.Loading -> {
                isLoginProgress = true
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LoginScreen(
        latestEmail = latestEmail,
        loginState = loginState,
        isEmailLogin = isEmailLogin,
        isLoginProgress = isLoginProgress,
        navigateToJoin = navigateToJoin,
        onClickGoogleLogin = {
            lifecycleOwner.lifecycleScope.launch {
                try {
                    getCredential(LoginPlatform.Google, context).getOrThrow().let {
                        loginViewModel.handleSignIn(it.credential)
                    }
                } catch (e: Exception) {
                    Log.e("porring_test_tag", "$e")
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
private fun LoginScreen(
    latestEmail: String = "",
    loginState: UiState<String> = UiState.Idle,
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
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            contentScale = ContentScale.Crop,
            painter = painterResource(drawable.bg_login),
            contentDescription = null
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {

            LoginContent(
                latestEmail = latestEmail,
                isEmailLogin = isEmailLogin,
                isLoginProgress = isLoginProgress,
                navigateToJoin = navigateToJoin,
                onClickGoogleLogin = onClickGoogleLogin,
                onClickEmailLogin = onClickEmailLogin,
                onClickEmailMode = onClickEmailMode
            )

            if (loginState is UiState.Idle || loginState is UiState.Failure) {
                PorringTopAppBar(navigationIcon = {
                    if (isEmailLogin) {
                        PorringIconButton(
                            icon = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                            onClick = cancelEmailMode,
                            contentDescription = stringResource(string.string_back_to_login)
                        )
                    }
                }, trailingIcon = {
                    PorringIconButton(
                        icon = Icons.Default.Close,
                        onClick = popBackStack,
                        contentDescription = stringResource(string.string_go_back)
                    )
                })
            }
        }
    }
}

@Composable
fun LoginContent(
    latestEmail: String,
    isEmailLogin: Boolean = false,
    isLoginProgress: Boolean = false,
    navigateToJoin: () -> Unit = {},
    onClickGoogleLogin: () -> Unit = {},
    onClickEmailLogin: (String, String) -> Unit = { _, _ -> },
    onClickEmailMode: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoItem()

        Spacer(modifier = Modifier.height(36.dp))

        if (isLoginProgress) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp), color = Primary
            )
        } else {
            Box {
                LoginButtonGroup(
                    PainterIconButton(
                        icon = drawable.logo_google,
                        text = stringResource(string.start_with_google),
                        onClick = onClickGoogleLogin
                    ), VectorIconButton(
                        icon = Icons.Default.Email, text = stringResource(string.string_email_login), onClick = onClickEmailMode
                    ), visible = isEmailLogin.not()
                )

                EmailLoginContent(
                    latestEmail = latestEmail,
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
    latestEmail: String = "",
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
            var emailText by remember { mutableStateOf(latestEmail) }
            var pwText by remember { mutableStateOf("") }
            val (idField, pwField) = FocusRequester.createRefs()
            var isLoginEnable by remember { mutableStateOf(false) }

            PorringTextField(
                value = emailText,
                onValueChange = {
                    emailText = it
                    isLoginEnable = emailText.isNotEmpty() && pwText.isNotEmpty()
                },
                hint = stringResource(string.string_input_email),
                label = stringResource(string.string_email),
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = {
                    pwField.requestFocus()
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .focusRequester(idField)
            )

            PorringTextField(
                value = pwText,
                onValueChange = {
                    pwText = it
                    isLoginEnable = emailText.isNotEmpty() && pwText.isNotEmpty()
                },
                hint = stringResource(string.string_input_pw),
                label = stringResource(string.string_pw),
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() },
                    onPrevious = { idField.requestFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .focusRequester(pwField)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                enabled = isLoginEnable,
                onClick = {
                    onClickEmailLogin(emailText, pwText)
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
                Text(text = stringResource(string.string_login))
            }

            TextButton(
                onClick = { navigateToJoin() },
            ) {
                Text(
                    text = stringResource(string.string_signup), color = Primary, style = MaterialTheme.typography.labelLarge
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
