package com.simats.genecare.data.repository

import com.simats.genecare.data.model.FamilyHistorySaveRequest
import com.simats.genecare.data.model.FamilyHistorySaveResponse
import com.simats.genecare.data.model.MedicalHistorySaveRequest
import com.simats.genecare.data.model.MedicalHistorySaveResponse
import com.simats.genecare.data.model.RiskAssessmentSaveRequest
import com.simats.genecare.data.model.RiskAssessmentSaveResponse
import com.simats.genecare.data.network.ApiClient
import retrofit2.Response

class OnboardingRepository {
    private val api = ApiClient.api

    suspend fun saveMedicalHistory(request: MedicalHistorySaveRequest): Response<MedicalHistorySaveResponse> {
        return api.saveMedicalHistory(request)
    }

    suspend fun saveFamilyHistory(request: FamilyHistorySaveRequest): Response<FamilyHistorySaveResponse> {
        return api.saveFamilyHistory(request)
    }

    suspend fun saveRiskAssessment(request: RiskAssessmentSaveRequest): Response<RiskAssessmentSaveResponse> {
        return api.saveRiskAssessment(request)
    }
}
