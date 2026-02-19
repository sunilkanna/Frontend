package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.UserSession
import com.simats.genecare.data.model.MedicalHistorySaveRequest
import com.simats.genecare.data.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OnboardingState {
    object Idle : OnboardingState()
    object Loading : OnboardingState()
    data class Success(val message: String) : OnboardingState()
    data class Error(val message: String) : OnboardingState()
}

class MedicalHistoryViewModel(private val repository: OnboardingRepository = OnboardingRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    val uiState = _uiState.asStateFlow()

    fun saveMedicalHistory(conditions: List<String>, notes: String) {
        val userId = UserSession.getUserId()
        if (userId == null) {
            _uiState.value = OnboardingState.Error("User session not found")
            return
        }

        viewModelScope.launch {
            _uiState.value = OnboardingState.Loading
            try {
                val request = MedicalHistorySaveRequest(
                    userId = userId,
                    condition = conditions.joinToString(", "),
                    medications = notes, // Using notes as medications for now
                    allergies = "",
                    surgeries = "",
                    diagnosisDate = ""
                )
                val response = repository.saveMedicalHistory(request)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _uiState.value = OnboardingState.Success(response.body()?.message ?: "Saved")
                } else {
                    _uiState.value = OnboardingState.Error(response.body()?.message ?: "Error saving history")
                }
            } catch (e: Exception) {
                _uiState.value = OnboardingState.Error(e.message ?: "Network error")
            }
        }
    }
}
