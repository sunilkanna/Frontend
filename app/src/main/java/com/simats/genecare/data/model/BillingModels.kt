package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class GetAppointmentDetailsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("appointment") val appointment: AppointmentDetailData?,
    @SerializedName("message") val message: String? = null
)

data class AppointmentDetailData(
    @SerializedName("id") val id: Int,
    @SerializedName("patient_id") val patientId: Int,
    @SerializedName("counselor_id") val counselorId: Int,
    @SerializedName("appointment_date") val appointmentDate: String,
    @SerializedName("time_slot") val timeSlot: String,
    @SerializedName("status") val status: String,
    @SerializedName("patient_name") val patientName: String?,
    @SerializedName("counselor_name") val counselorName: String?,
    @SerializedName("consultation_fee") val consultationFee: Double?
)

data class CompleteAppointmentRequest(
    @SerializedName("appointment_id") val appointmentId: Int
)

data class CreatePaymentRequest(
    @SerializedName("appointment_id") val appointmentId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("payment_method") val paymentMethod: String
)

data class CreatePaymentResponse(
    @SerializedName("status") val status: String,
    @SerializedName("payment_id") val paymentId: Int?,
    @SerializedName("message") val message: String?
)

data class UpdatePaymentStatusRequest(
    @SerializedName("payment_id") val paymentId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("transaction_id") val transactionId: String?
)
