package com.kolown.model


sealed class SnackBarData {
    abstract val message: String
    abstract val actionLabel: String?

    class LoginRequired(
    ) : SnackBarData() {
        override val message: String = "로그인 후 이용 가능한 서비스입니다."
        override val actionLabel: String = "로그인"
    }

    data class Message(
        override val message: String,
        override val actionLabel: String?,
    ) : SnackBarData()
}
