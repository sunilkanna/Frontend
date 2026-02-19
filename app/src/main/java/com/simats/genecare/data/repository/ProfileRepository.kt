package com.simats.genecare.data.repository

import com.simats.genecare.data.model.ProfileUpdateRequest
import com.simats.genecare.data.model.ProfileUpdateResponse
import com.simats.genecare.data.network.ApiClient
import com.simats.genecare.data.network.GeneCareApi
import retrofit2.Response

class ProfileRepository(private val api: GeneCareApi = ApiClient.api) {
    suspend fun updateProfile(request: ProfileUpdateRequest): Response<ProfileUpdateResponse> {
        return api.updateProfile(request)
    }
}
