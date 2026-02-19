package com.simats.genecare.ui

import com.simats.genecare.data.model.User

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String, val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
