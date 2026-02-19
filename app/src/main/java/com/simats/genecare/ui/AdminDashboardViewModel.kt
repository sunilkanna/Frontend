package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.model.AdminStatsResponse
import com.simats.genecare.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminDashboardViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _adminStats = MutableStateFlow<AdminStatsResponse?>(null)
    val adminStats: StateFlow<AdminStatsResponse?> = _adminStats.asStateFlow()

    init {
        fetchAdminStats()
    }

    fun fetchAdminStats() {
        viewModelScope.launch {
            try {
                val response = authRepository.getAdminStats()
                if (response.isSuccessful && response.body()?.status == "success") {
                    _adminStats.value = response.body()
                } else {
                    // Handle error (optional: add error state)
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}
