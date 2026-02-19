package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val lastVisit: String,
    val condition: String,
    val status: String // "Active", "Pending", "Completed"
)

data class PatientListState(
    val patients: List<Patient> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

class PatientListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PatientListState())
    val uiState: StateFlow<PatientListState> = _uiState.asStateFlow()


    private val repository = com.simats.genecare.data.repository.AuthRepository()

    init {
        loadPatients()
    }

    private fun loadPatients() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Use logged in user ID, or default to 1 for demo
                val userId = com.simats.genecare.data.UserSession.getUserId() ?: 1
                val response = repository.getCounselorPatients(userId)
                
                if (response.isSuccessful && response.body()?.status == "success") {
                    val patientList = response.body()?.patients ?: emptyList()
                    val mappedPatients = patientList.map { 
                        Patient(
                            id = it.id,
                            name = it.name,
                            age = it.age ?: 0,
                            gender = it.gender ?: "Unknown",
                            lastVisit = it.lastVisit,
                            condition = it.condition ?: "None",
                            status = it.status
                        )
                    }
                     _uiState.value = PatientListState(
                        patients = mappedPatients,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false) // Handle error
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
