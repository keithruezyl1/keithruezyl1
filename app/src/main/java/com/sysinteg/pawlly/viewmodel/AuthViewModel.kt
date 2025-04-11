package com.sysinteg.pawlly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sysinteg.pawlly.model.AuthResponse
import com.sysinteg.pawlly.model.User
import com.sysinteg.pawlly.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signUp(user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signUp(user)
                .onSuccess { response ->
                    if (response.success) {
                        _authState.value = AuthState.Success(user)
                    } else {
                        _authState.value = AuthState.Error(response.message)
                    }
                }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Unknown error")
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.login(email, password)
                .onSuccess { response ->
                    if (response.success && response.token != null) {
                        _authState.value = AuthState.Authenticated(response.token)
                    } else {
                        _authState.value = AuthState.Error(response.message)
                    }
                }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Unknown error")
                }
        }
    }

    fun getCurrentUser(token: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.getCurrentUser(token)
                .onSuccess { user ->
                    _authState.value = AuthState.Success(user)
                }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Unknown error")
                }
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Authenticated(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
} 