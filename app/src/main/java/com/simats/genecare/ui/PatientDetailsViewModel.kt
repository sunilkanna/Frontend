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
    val isLoading: Boolean = false
)

class PatientDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PatientDetailsState())
    val uiState: StateFlow<PatientDetailsState> = _uiState.asStateFlow()

    private val repository = com.simats.genecare.data.repository.AuthRepository()

    fun loadPatient(patientId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Simulate network delay
            // delay(500)
            
            // Dummy data generation based on ID (simplified for demo)
            val dummyPatient = PatientDetails(
                id = patientId,
                name = "Emily Rodriguez", // We'd normally fetch real name by ID
                age = 32,
                gender = "Female",
                email = "emily.rodriguez@example.com",
                phone = "+1 (555) 123-4567",
                address = "123 Maple Ave, Springfield",
                bloodType = "A+",
                height = "165 cm",
                weight = "58 kg",
                medicalHistory = listOf(
                    "Family history of breast cancer (Mother, Aunt)",
                    "Mild Asthma",
                    "Allergic to Penicillin"
                ),
                geneticRisks = listOf(
                    RiskItem("Breast Cancer (BRCA1)", "High"),
                    RiskItem("Ovarian Cancer", "Moderate"),
                    RiskItem("Lynch Syndrome", "Low")
                ),
                recentNotes = listOf(
                    NoteItem("Feb 08, 2026", "Patient discussed improved diet plans."),
                    NoteItem("Jan 15, 2026", "Initial consultation. Ordered BRCA panel.")
                )
            )

            _uiState.value = PatientDetailsState(
                patient = dummyPatient,
                isLoading = false
            )
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
