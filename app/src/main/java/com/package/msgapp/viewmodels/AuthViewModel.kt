package com.package.msgapp.viewmodels

import androidx.lifecycle.ViewModel
import com.package.msgapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        authRepository.login(email, password) { success, error ->
            _authState.value = if (success) AuthState.Success else AuthState.Error(error ?: "Login failed")
        }
    }

    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading
        authRepository.register(email, password) { success, error ->
            _authState.value = if (success) AuthState.Success else AuthState.Error(error ?: "Registration failed")
        }
    }
}
