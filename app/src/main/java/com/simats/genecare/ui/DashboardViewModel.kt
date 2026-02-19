package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.UserSession
import com.simats.genecare.data.model.DashboardStatsResponse
import com.simats.genecare.data.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(val stats: DashboardStatsResponse) : DashboardState()
    data class Error(val message: String) : DashboardState()
}

class DashboardViewModel(private val repository: DashboardRepository = DashboardRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            UserSession.currentUser.collect { user ->
                if (user != null) {
                    fetchStats()
                }
            }
        }
    }

    fun fetchStats() {
        val user = UserSession.getUser()
        if (user == null) {
            _uiState.value = DashboardState.Error("User session not found")
            return
        }

        viewModelScope.launch {
            _uiState.value = DashboardState.Loading
            try {
                // Fix: Use user.userType (camelCase) instead of user_type
                // Fix: DashboardStatsResponse might need to have 'message' field handled if null
                val response = repository.getDashboardStats(user.id, user.userType)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _uiState.value = DashboardState.Success(response.body()!!)
                } else {
                    _uiState.value = DashboardState.Error(response.body()?.message ?: "Error fetching dashboard data")
                }
            } catch (e: Exception) {
                _uiState.value = DashboardState.Error(e.message ?: "Network error")
            }
        }
    }
}
