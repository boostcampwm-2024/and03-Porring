package com.kolown.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AuthRepository
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private var _joinState: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Idle)
    val joinState = _joinState.asStateFlow()

    fun joinWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _joinState.update { UiState.Loading }

            delay(500)

            authRepository.joinWithEmailAndPassword(email, password)
                .onSuccess { _joinState.update { UiState.Success("회원가입이 완료 되었습니다.") } }
                .onFailure { e -> _joinState.update { UiState.Failure(e) } }
        }
    }
}