package com.simats.genecare.data.repository

import com.simats.genecare.data.model.DashboardStatsRequest
import com.simats.genecare.data.model.DashboardStatsResponse
import com.simats.genecare.data.network.ApiClient
import retrofit2.Response

class DashboardRepository {
    private val api = ApiClient.api

    suspend fun getDashboardStats(userId: Int, userType: String): Response<DashboardStatsResponse> {
        val request = DashboardStatsRequest(userId, userType)
        return api.getDashboardStats(request)
    }
}
