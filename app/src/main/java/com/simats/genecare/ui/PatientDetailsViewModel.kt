package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PatientDetails(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val address: String,
    val bloodType: String,
    val height: String,
    val weight: String,
    val medicalHistory: List<String>,
    val geneticRisks: List<RiskItem>,
    val recentNotes: List<NoteItem>
)

data class RiskItem(val condition: String, val level: String) // level: Low, Moderate, High
data class NoteItem(val date: String, val content: String)

data class PatientDetailsState(
    val patient: PatientDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PatientDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PatientDetailsState())
    val uiState: StateFlow<PatientDetailsState> = _uiState.asStateFlow()

    private val repository = com.simats.genecare.data.repository.AuthRepository()

    fun loadPatient(patientId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.getPatientDetails(patientId.toInt())
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        val p = body.patient
                        if (p != null) {
                            val details = PatientDetails(
                                id = p.id.toString(),
                                name = p.name ?: "Unknown",
                                age = p.age ?: 0,
                                gender = p.gender ?: "Unknown",
                                email = p.email ?: "",
                                phone = p.phone ?: "",
                                address = p.address ?: "",
                                bloodType = p.bloodType ?: "N/A",
                                height = p.height ?: "N/A",
                                weight = p.weight ?: "N/A",
                                medicalHistory = p.medicalHistory?.map { it.conditionName } ?: emptyList(),
                                geneticRisks = p.geneticRisks?.map { 
                                    RiskItem(it.category, "Assessed: ${it.assessedAt}")
                                 } ?: emptyList(),
                                recentNotes = emptyList()
                            )
                            _uiState.value = PatientDetailsState(patient = details, isLoading = false)
                        } else {
                            _uiState.value = PatientDetailsState(errorMessage = "Patient data is missing", isLoading = false)
                        }
                    } else {
                        _uiState.value = PatientDetailsState(errorMessage = body?.message ?: "Failed to load patient profile", isLoading = false)
                    }
                } else {
                    _uiState.value = PatientDetailsState(errorMessage = "Server error: ${response.code()}", isLoading = false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = PatientDetailsState(errorMessage = "Connection error: ${e.message}", isLoading = false)
            }
        }
    }

    fun uploadReport(patientId: String, uri: android.net.Uri, context: android.content.Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val file = getFileFromUri(context, uri)
                if (file != null) {
                    val response = repository.uploadReport(patientId.toInt(), file)
                    if (response.isSuccessful && response.body()?.status == "success") {
                        // Success toast or state update
                        // Ideally trigger a refresh of reports list if displayed
                    } else {
                        // Error handling
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun getFileFromUri(context: android.content.Context, uri: android.net.Uri): java.io.File? {
        val contentResolver = context.contentResolver
        val tempFile = java.io.File(context.cacheDir, "temp_upload_${System.currentTimeMillis()}")
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = java.io.FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            return tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
