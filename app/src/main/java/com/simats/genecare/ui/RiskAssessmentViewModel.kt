package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.UserSession
import com.simats.genecare.data.model.RiskAssessmentSaveRequest
import com.simats.genecare.data.repository.OnboardingRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RiskAssessmentViewModel(private val repository: OnboardingRepository = OnboardingRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<OnboardingState>(OnboardingState.Idle)
    val uiState = _uiState.asStateFlow()

    fun saveAssessment(answers: Map<String, String>) {
        val userId = UserSession.getUserId()
        if (userId == null) {
            _uiState.value = OnboardingState.Error("User session not found")
            return
        }

        viewModelScope.launch {
            _uiState.value = OnboardingState.Loading
            try {
                // Calculate basic risk score (for demo purposes)
                var score = 0
                if (answers.values.any { it == "Yes" || it == "Yes, very concerned" }) {
                    score = 75
                } else if (answers.values.any { it == "Somewhat concerned" }) {
                    score = 40
                } else {
                    score = 15
                }

                val category = when {
                    score > 70 -> "High Risk"
                    score > 30 -> "Moderate Risk"
                    else -> "Low Risk"
                }

                val detailsJson = Gson().toJson(answers)

                val request = RiskAssessmentSaveRequest(
                    patientId = userId,
                    riskScore = score,
                    riskCategory = category,
                    details = detailsJson
                )
                
                val response = repository.saveRiskAssessment(request)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _uiState.value = OnboardingState.Success(response.body()?.message ?: "Assessment complete")
                } else {
                    _uiState.value = OnboardingState.Error(response.body()?.message ?: "Error saving assessment")
                }
            } catch (e: Exception) {
                _uiState.value = OnboardingState.Error(e.message ?: "Network error")
            }
        }
    }
}
