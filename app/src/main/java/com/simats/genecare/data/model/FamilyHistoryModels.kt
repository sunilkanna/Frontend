package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class FamilyHistorySaveRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("relation") val relation: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("description") val description: String
)

data class FamilyHistorySaveResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)
