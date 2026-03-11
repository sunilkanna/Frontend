package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class GetPatientDetailsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("patient") val patient: PatientDetailItem?
)

data class PatientDetailItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    @SerializedName("height") val height: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("blood_type") val bloodType: String?,
    @SerializedName("medical_history") val medicalHistory: List<MedicalHistoryItem>?,
    @SerializedName("genetic_risks") val geneticRisks: List<GeneticRiskItem>?
)

data class MedicalHistoryItem(
    @SerializedName("id") val id: Int,
    @SerializedName("condition_name") val conditionName: String,
    @SerializedName("diagnosis_date") val diagnosisDate: String,
    @SerializedName("medications") val medications: String?,
    @SerializedName("allergies") val allergies: String?,
    @SerializedName("surgeries") val surgeries: String?
)

data class GeneticRiskItem(
    @SerializedName("category") val category: String,
    @SerializedName("assessed_at") val assessedAt: String
)
