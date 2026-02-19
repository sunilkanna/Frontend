package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.repository.AuthRepository
import com.simats.genecare.data.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class CounselorReportsViewModel : ViewModel() {
    private val _reports = MutableStateFlow<List<com.simats.genecare.data.model.CounselorReportItem>>(emptyList())
    val reports: StateFlow<List<com.simats.genecare.data.model.CounselorReportItem>> = _reports.asStateFlow()
    private val repository = AuthRepository()

    init {
        loadReports()
    }

    private fun loadReports() {
        val user = UserSession.getUser() ?: return
        viewModelScope.launch {
            try {
                val response = repository.getCounselorReports(user.id)
                if (response.isSuccessful && response.body()?.status == "success") {
                     _reports.value = response.body()?.reports ?: emptyList()
                }
            } catch (e: Exception) {
                 e.printStackTrace()
            }
        }
    }
}
