package com.kolown.login

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.kolown.login.component.ButtonWithIcon
import com.kolown.login.component.LoginButtonGroup
import com.kolown.login.component.LoginTopAppBar
import com.kolown.login.component.LogoItem
import com.kolown.login.util.LoginButton
import com.kolown.login.util.LoginButton.*
import com.kolown.login.util.LoginPlatform
import com.kolown.login.util.getCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun LoginRoute(
    updateLoginState: () -> Unit,
    popBackStack: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    padding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(true) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            loginViewModel.loginEnd.collect { loginComplete ->
                if (loginComplete) {
                    updateLoginState()
                    popBackStack()
                }
            }
        }
    }

    LoginScreen(
        onClickGoogleLogin = {
            CoroutineScope(Dispatchers.Main).launch {
                getCredential(LoginPlatform.Google, context).getOrNull()?.let {
                    loginViewModel.handleSignIn(it)
                }
            }
        },
        popBackStack = popBackStack,
        padding = padding
    )
}

@Composable
fun LoginScreen(
    onClickGoogleLogin: () -> Unit = {},
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
    ) {
        Image(
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.bg_login),
            contentDescription = null
        )

        LoginContent(
            onClickGoogleLogin = onClickGoogleLogin
        )

        LoginTopAppBar(
            popBackStack = popBackStack
        )
    }
}

@Composable
fun LoginContent(
    onClickGoogleLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoItem()

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = stringResource(R.string.login_singup), fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        LoginButtonGroup (
            PainterIconButton(
                icon = R.drawable.logo_google,
                text = stringResource(R.string.start_with_google),
                onClick = onClickGoogleLogin
            ),
            VectorIconButton(
                icon = Icons.Default.Email,
                text = "이메일로 로그인",
                onClick = onClickGoogleLogin
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLoginScreen() {
    LoginScreen()
}
