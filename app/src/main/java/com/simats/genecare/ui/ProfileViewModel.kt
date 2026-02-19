package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.UserSession
import com.simats.genecare.data.model.ProfileUpdateRequest
import com.simats.genecare.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val message: String) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(private val repository: ProfileRepository = ProfileRepository()) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState = _profileState.asStateFlow()

    fun updateProfile(fullName: String, dob: String, gender: String, phone: String, address: String) {
        val userId = UserSession.getUserId()
        if (userId == null) {
            _profileState.value = ProfileState.Error("User session not found. Please log in again.")
            return
        }
        
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val request = ProfileUpdateRequest(
                    userId = userId,
                    fullName = fullName,
                    dateOfBirth = dob,
                    gender = gender,
                    phone = phone,
                    address = address
                )
                val response = repository.updateProfile(request)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _profileState.value = ProfileState.Success(response.body()?.message ?: "Profile updated")
                } else {
                    _profileState.value = ProfileState.Error(response.body()?.message ?: "Failed to update profile")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Network error")
            }
        }
    }
}
