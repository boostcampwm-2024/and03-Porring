package com.porring.login

import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel : ViewModel() {

    fun googleLogin(
        activityResult : ActivityResult,
        onSuccess : () -> Unit
    ) {
        try {
            val account = GoogleSignIn
                .getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        onSuccess()
                        //토큰
                        val token = account.idToken
                        //사용자 이메일
                        val email = task.result.user?.email
                    }
                }
        } catch (e:Exception){
            Log.e("로그인 실패",e.message.toString())
        }
    }
}