package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TestResult(
    val title: String,
    val status: String, // "Normal", "Moderate", "High"
    val date: String,
    val description: String
)

data class MyResultsState(
    val overallRiskLevel: String = "Low Risk",
    val overallRiskScore: Int = 15,
    val lastUpdated: String = "Feb 5, 2026",
    val testResults: List<TestResult> = emptyList(),
    val isLoading: Boolean = false
)

class MyResultsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyResultsState())
    val uiState: StateFlow<MyResultsState> = _uiState.asStateFlow()

    init {
        loadResults()
    }

    private fun loadResults() {
        val user = com.simats.genecare.data.UserSession.getUser() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = com.simats.genecare.data.network.ApiClient.api.getPatientResults(user.id)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val body = response.body()
                    val risk = body?.risk_assessment
                    
                    val results = body?.reports?.map { report ->
                        TestResult(
                            title = report.title,
                            status = report.status,
                            date = report.date,
                            description = report.description
                        )
                    } ?: emptyList()

                    _uiState.value = MyResultsState(
                        overallRiskLevel = risk?.risk_category ?: "Not Assessed",
                        overallRiskScore = risk?.risk_score ?: 0,
                        lastUpdated = risk?.assessed_at ?: "Never",
                        testResults = results,
                        isLoading = false
                    )
                } else {
                     _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
