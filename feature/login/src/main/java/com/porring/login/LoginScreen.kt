package com.porring.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.login.BuildConfig
import com.app.login.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {

    val loginViewModel = LoginViewModel()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        loginViewModel.googleLogin(activityResult = it) {
            Toast.makeText(context, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title ={})
        }
    ) { innerPadding ->
        LoginContent(onClick = {
            val token = BuildConfig.GOOGLE_CLIENT_ID
            val googleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
            launcher.launch(googleSignInClient.signInIntent)
        }, contentPadding = innerPadding)
    }
}

@Composable
fun LoginContent(
    onClick: () -> Unit = {},
    contentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
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
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "✨ 순간으로 이어지는 진짜 연결의 시작 ✨", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "로그인/회원가입", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))
        ButtonWithIcon(icon = R.drawable.logo_google, text = "구글로 시작하기", onClick = onClick)
    }
}

@Composable
fun ButtonWithIcon(
    icon: Int,
    text: String,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(width = 252.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Gray
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
    LoginContent(contentPadding = PaddingValues(0.dp))
}