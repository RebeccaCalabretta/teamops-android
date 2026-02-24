package io.github.rebeccacalabretta.teamops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rebeccacalabretta.teamops.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSessionViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state: MutableStateFlow<UserSessionState> =
        MutableStateFlow(UserSessionState())

    val state: StateFlow<UserSessionState> =
        _state.asStateFlow()

    init {
        loadSession()
    }

    private fun loadSession() {
        val uid: String? = auth.currentUser?.uid

        if (uid == null) {
            _state.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {
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