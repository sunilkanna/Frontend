package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class RiskAssessmentSaveRequest(
    @SerializedName("patient_id") val patientId: Int,
    @SerializedName("risk_score") val riskScore: Int,
    @SerializedName("risk_category") val riskCategory: String,
    @SerializedName("details") val details: String // JSON string
)

data class RiskAssessmentSaveResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)
