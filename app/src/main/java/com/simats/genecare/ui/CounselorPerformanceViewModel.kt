package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.repository.AuthRepository
import com.simats.genecare.data.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PerformanceMetric(
    val title: String,
    val value: String,
    val trend: String,
    val isPositive: Boolean
)

class CounselorPerformanceViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _metrics = MutableStateFlow<List<PerformanceMetric>>(emptyList())
    val metrics: StateFlow<List<PerformanceMetric>> = _metrics.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchMetrics()
    }

    private fun fetchMetrics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = UserSession.getUserId() ?: 1
                val response = repository.getCounselorAnalytics(userId)
                
                if (response.isSuccessful && response.body()?.status == "success") {
                    val data = response.body()!!
                    
                    _metrics.value = listOf(
                        PerformanceMetric(
                            title = "Total Sessions",
                            value = data.totalSessions.toString(),
                            trend = "+5%", // Mock trend for now
                            isPositive = true
                        ),
                        PerformanceMetric(
                            title = "Patient Satisfaction",
                            value = "${data.averageRating}/5.0",
                            trend = "+0.2",
                            isPositive = true
                        ),
                        PerformanceMetric(
                            title = "Total Earnings",
                            value = "₹${String.format("%.2f", data.totalEarnings)}",
                            trend = "+12%",
                            isPositive = true
                        ),
                        PerformanceMetric(
                            title = "Total Patients",
                            value = data.totalPatients.toString(),
                            trend = "+2",
                            isPositive = true
                        )
                    )
                } else {
                    // Handle error or use defaults
                    _metrics.value = listOf(
                        PerformanceMetric("Total Sessions", "0", "0%", false),
                        PerformanceMetric("Patient Satisfaction", "0.0/5.0", "0.0", false),
                        PerformanceMetric("Total Earnings", "₹0.00", "0%", false),
                        PerformanceMetric("Total Patients", "0", "0", false)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Error state
            } finally {
                _isLoading.value = false
            }
        }
    }
}
