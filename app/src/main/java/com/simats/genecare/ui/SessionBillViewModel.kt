package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.model.AppointmentDetailData
import com.simats.genecare.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SessionBillViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _appointmentDetails = MutableStateFlow<AppointmentDetailData?>(null)
    val appointmentDetails: StateFlow<AppointmentDetailData?> = _appointmentDetails.asStateFlow()

    private val _billDetails = MutableStateFlow<BillDetails?>(null)
    val billDetails: StateFlow<BillDetails?> = _billDetails.asStateFlow()

    private val _paymentState = MutableStateFlow<String>("Idle") // Idle, Processing, Success, Error
    val paymentState: StateFlow<String> = _paymentState.asStateFlow()

    data class BillDetails(
        val consultationFee: Double,
        val platformFee: Double,
        val gst: Double,
        val totalAmount: Double
    )

    fun loadBill(appointmentId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getAppointmentDetails(appointmentId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val appt = response.body()?.appointment
                    _appointmentDetails.value = appt
                    
                    if (appt != null) {
                        val fee = appt.consultationFee ?: 1000.0 // Default fallback
                        val platform = 50.0
                        val gst = (fee + platform) * 0.18
                        val total = fee + platform + gst
                        
                        _billDetails.value = BillDetails(
                            consultationFee = fee,
                            platformFee = platform,
                            gst = gst,
                            totalAmount = total
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun processPayment(appointmentId: Int, method: String, onSuccess: () -> Unit) {
        _paymentState.value = "Processing"
        viewModelScope.launch {
            try {
                val amount = _billDetails.value?.totalAmount ?: 0.0
                val response = repository.createPayment(appointmentId, amount, method)
                
                if (response.isSuccessful && response.body()?.status == "success") {
                    val paymentId = response.body()?.paymentId
                    if (paymentId != null) {
                        // Simulate success from gateway
                        val updateResponse = repository.updatePaymentStatus(paymentId, "Completed", "TXN-${System.currentTimeMillis()}")
                         if (updateResponse.isSuccessful && updateResponse.body()?.status == "success") {
                             _paymentState.value = "Success"
                             onSuccess()
                         } else {
                             _paymentState.value = "Error: Payment Update Failed"
                         }
                    } else {
                        _paymentState.value = "Error: Payment Creation Failed"
                    }
                } else {
                    _paymentState.value = "Error: API Failed"
                }
            } catch (e: Exception) {
                _paymentState.value = "Error: ${e.message}"
            }
        }
    }
}
