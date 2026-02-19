package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.model.LoginRequest
import com.simats.genecare.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body()?.status == "success") {
                    val user = response.body()?.user
                    if (user != null) {
                        // FIX: Save session
                        com.simats.genecare.data.UserSession.login(user)
                        
                        _loginState.value = LoginState.Success(
                            message = response.body()?.message ?: "Success",
                            user = user
                        )
                    } else {
                        _loginState.value = LoginState.Error(response.body()?.message ?: "Login failed: User data is null")
                    }
                } else {
                    _loginState.value = LoginState.Error(response.body()?.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Network error")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
