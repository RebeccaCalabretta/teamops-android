package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.domain.repository.AuthRepository
import io.github.rebeccacalabretta.teamops.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSessionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserSessionState())
    val state: StateFlow<UserSessionState> = _state.asStateFlow()

    init {
        observeSession()
    }

    fun logout() = authRepository.logout()

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.observeAuthState().collectLatest { uid ->
                if (uid == null) {
                    _state.update { it.copy(isLoading = false, session = null) }
                    return@collectLatest
                }

                _state.update { it.copy(isLoading = true) }

                val session = userRepository.getUserSession(uid)

                _state.update {
                    it.copy(
                        isLoading = false,
                        session = session
                    )
                }
            }
        }
    }
}