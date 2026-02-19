package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class CounselorAnalyticsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("total_sessions") val totalSessions: Int,
    @SerializedName("total_patients") val totalPatients: Int,
    @SerializedName("total_earnings") val totalEarnings: Double,
    @SerializedName("average_rating") val averageRating: Double,
    @SerializedName("rating_count") val ratingCount: Int
)
