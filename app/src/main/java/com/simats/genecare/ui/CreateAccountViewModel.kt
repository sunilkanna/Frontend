package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateAccountViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun register(fullName: String, email: String, password: String, userType: String = "Patient") {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            try {
                val response = repository.register(fullName, email, password, userType)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _registrationState.value = RegistrationState.Success(
                        message = response.body()?.message ?: "Success",
                        user = response.body()?.user
                    )
                } else {
                    _registrationState.value = RegistrationState.Error(response.body()?.message ?: "Registration failed")
                }
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error(e.message ?: "Network error")
            }
        }
    }
    
    fun resetState() {
        _registrationState.value = RegistrationState.Idle
    }
}

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    data class Success(val message: String, val user: com.simats.genecare.data.model.User? = null) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}
