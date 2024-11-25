package com.kolown.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Spacer(modifier = Modifier.height(50.dp))
    LoginScreen(
        onClickGoogleLogin = {
            CoroutineScope(Dispatchers.Main).launch {
                getCredential(LoginPlatform.Google, context).getOrNull()?.let {
                    loginViewModel.handleSignIn(it)
                }
            }
        }, popBackStack = popBackStack, padding = padding
    )
}

@Composable
fun LoginScreen(
    onClickGoogleLogin: () -> Unit = {},
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.string_close_button)
                )
            }
        }
        Spacer(modifier = Modifier.height(200.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.logo_icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.logo_porring),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(R.string.app_sub_title), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = stringResource(R.string.login_singup), fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithIcon(
            icon = R.drawable.logo_google,
            text = stringResource(R.string.start_with_google),
            onClick = onClickGoogleLogin
        )
    }
}

@Composable
fun ButtonWithIcon(
    icon: Int,
    text: String,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(width = 252.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, contentColor = Color.Gray
        )
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = Color.Unspecified)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLoginScreen() {
    LoginScreen()
}
