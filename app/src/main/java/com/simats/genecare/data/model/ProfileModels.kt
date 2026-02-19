package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String
)

data class ProfileUpdateResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)
