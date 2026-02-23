package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.domain.model.AuthResult
import io.github.rebeccacalabretta.teamops.domain.model.LoginError
import io.github.rebeccacalabretta.teamops.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: LoginError? = null
) {
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                !isLoading
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                error = null
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                error = null
            )
        }
    }

    fun login() {
        if (!_uiState.value.isLoginEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.login(
                email = _uiState.value.email,
                password = _uiState.value.password
            )

            when (result) {
                AuthResult.Success ->
                    _uiState.update { it.copy(isLoading = false) }

                AuthResult.Failure.InvalidCredentials ->
                    _uiState.update {
                        it.copy(isLoading = false, error = LoginError.InvalidCredentials)
                    }

                AuthResult.Failure.UserNotFound ->
                    _uiState.update {
                        it.copy(isLoading = false, error = LoginError.UserNotFound)
                    }

                AuthResult.Failure.NetworkError ->
                    _uiState.update {
                        it.copy(isLoading = false, error = LoginError.Network)
                    }

                AuthResult.Failure.Unknown ->
                    _uiState.update {
                        it.copy(isLoading = false, error = LoginError.Unknown)
                    }
            }
        }
    }
}