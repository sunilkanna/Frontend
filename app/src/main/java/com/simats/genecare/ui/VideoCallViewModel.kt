package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.model.AppointmentDetailData
import com.simats.genecare.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoCallViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _appointmentDetails = MutableStateFlow<AppointmentDetailData?>(null)
    val appointmentDetails: StateFlow<AppointmentDetailData?> = _appointmentDetails.asStateFlow()

    private val _callStatus = MutableStateFlow<String>("Connecting...")
    val callStatus: StateFlow<String> = _callStatus.asStateFlow()

    fun fetchAppointmentDetails(appointmentId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getAppointmentDetails(appointmentId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _appointmentDetails.value = response.body()?.appointment
                    _callStatus.value = "Connected"
                } else {
                    _callStatus.value = "Failed to load details"
                }
            } catch (e: Exception) {
                _callStatus.value = "Error: ${e.message}"
            }
        }
    }

    fun endCall(appointmentId: Int, onCallEnded: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.completeAppointment(appointmentId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    onCallEnded()
                } else {
                    // Handle failure, maybe force end anyway
                    onCallEnded() 
                }
            } catch (e: Exception) {
                onCallEnded()
            }
        }
    }
}
