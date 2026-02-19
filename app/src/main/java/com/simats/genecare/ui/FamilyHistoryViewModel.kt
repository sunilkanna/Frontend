package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.UserSession
import com.simats.genecare.data.model.FamilyHistorySaveRequest
import com.simats.genecare.data.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FamilyHistoryViewModel(private val repository: OnboardingRepository = OnboardingRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    val uiState = _uiState.asStateFlow()

    fun saveFamilyHistory(members: List<FamilyMember>) {
        val userId = UserSession.getUserId()
        if (userId == null) {
            _uiState.value = OnboardingState.Error("User session not found")
            return
        }

        viewModelScope.launch {
            _uiState.value = OnboardingState.Loading
            try {
                // Save each member
                var allSuccess = true
                var errorMessage = ""

                for (member in members) {
                    val request = FamilyHistorySaveRequest(
                        userId = userId,
                        relation = member.relation,
                        condition = member.conditions,
                        description = member.role
                    )
                    val response = repository.saveFamilyHistory(request)
                    if (!response.isSuccessful || response.body()?.status != "success") {
                        allSuccess = false
                        errorMessage = response.body()?.message ?: "Error saving member: ${member.relation}"
                        break
                    }
                }

                if (allSuccess) {
                    _uiState.value = OnboardingState.Success("Family history saved successfully")
                } else {
                    _uiState.value = OnboardingState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _uiState.value = OnboardingState.Error(e.message ?: "Network error")
            }
        }
    }
}
