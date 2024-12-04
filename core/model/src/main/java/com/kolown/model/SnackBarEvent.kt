package com.kolown.model


sealed class SnackBarEvent {
    abstract val message: String
    abstract val actionLabel: String?

    class LoginRequired : SnackBarEvent() {
        override val message: String = "로그인 후 이용 가능한 서비스입니다."
        override val actionLabel: String = "로그인"
    }

    data class Message(
        override val message: String,
        override val actionLabel: String?,
    ) : SnackBarEvent()
}
