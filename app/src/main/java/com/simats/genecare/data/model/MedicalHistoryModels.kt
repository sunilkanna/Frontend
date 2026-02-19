package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class MedicalHistorySaveRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("condition") val condition: String,
    @SerializedName("medications") val medications: String,
    @SerializedName("allergies") val allergies: String,
    @SerializedName("surgeries") val surgeries: String,
    @SerializedName("diagnosis_date") val diagnosisDate: String? = null
)

data class MedicalHistorySaveResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)
