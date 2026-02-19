package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.simats.genecare.data.network.ApiClient

data class AnalyticsState(
    val totalUsers: String = "0",
    val activeSessions: String = "0",
    val userGrowthData: List<Float> = emptyList(),
    val sessionDistributionData: List<Float> = emptyList(),
    val sessionDistributionLabels: List<String> = emptyList(),
    val userDemographicsData: List<Float> = emptyList(),
    val isLoading: Boolean = false
)

class AnalyticsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsState())
    val uiState: StateFlow<AnalyticsState> = _uiState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = ApiClient.api.getAnalytics()
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    _uiState.value = AnalyticsState(
                        totalUsers = data.totalUsers,
                        activeSessions = data.activeSessions,
                        userGrowthData = data.userGrowthData,
                        sessionDistributionData = data.sessionDistributionData,
                        sessionDistributionLabels = data.sessionDistributionLabels,
                        userDemographicsData = data.userDemographicsData,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                e.printStackTrace()
            }
        }
    }
}
