package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.UserSession
import com.simats.genecare.data.model.ProfileUpdateRequest
import com.simats.genecare.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileData(
    val fullName: String = "",
    val dob: String = "",
    val gender: String = "",
    val phone: String = "",
    val address: String = "",
    val height: String = "",
    val weight: String = "",
    val bloodType: String = ""
)

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val message: String) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val authRepository: com.simats.genecare.data.repository.AuthRepository = com.simats.genecare.data.repository.AuthRepository()
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState = _profileState.asStateFlow()

    private val _profileData = MutableStateFlow(ProfileData())
    val profileData = _profileData.asStateFlow()

    fun loadProfile() {
        val userId = UserSession.getUserId() ?: return
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val response = authRepository.getPatientDetails(userId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val p = response.body()?.patient
                    if (p != null) {
                        _profileData.value = ProfileData(
                            fullName = p.name ?: "",
                            dob = p.dateOfBirth ?: "",
                            gender = p.gender ?: "",
                            phone = p.phone ?: "",
                            address = p.address ?: "",
                            height = p.height ?: "",
                            weight = p.weight ?: "",
                            bloodType = p.bloodType ?: ""
                        )
                        _profileState.value = ProfileState.Idle
                    } else {
                        _profileState.value = ProfileState.Error("Profile data not found")
                    }
                } else {
                    _profileState.value = ProfileState.Error("Failed to load profile")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Network error")
            }
        }
    }

    fun updateProfile(fullName: String, dob: String, gender: String, phone: String, address: String, height: String = "", weight: String = "", bloodType: String = "") {
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
                    address = address,
                    height = height,
                    weight = weight,
                    bloodType = bloodType
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
