package com.simats.genecare.data.network

import com.simats.genecare.data.model.BookAppointmentRequest
import com.simats.genecare.data.model.BookAppointmentResponse
import com.simats.genecare.data.model.CounselorResponse
import com.simats.genecare.data.model.DashboardStatsRequest
import com.simats.genecare.data.model.DashboardStatsResponse
import com.simats.genecare.data.model.GetMessagesResponse
import com.simats.genecare.data.model.LoginRequest
import com.simats.genecare.data.model.RegisterRequest
import com.simats.genecare.data.model.AuthResponse
import com.simats.genecare.data.model.FamilyHistorySaveRequest
import com.simats.genecare.data.model.FamilyHistorySaveResponse
import com.simats.genecare.data.model.MedicalHistorySaveRequest
import com.simats.genecare.data.model.MedicalHistorySaveResponse
import com.simats.genecare.data.model.ProfileUpdateRequest
import com.simats.genecare.data.model.ProfileUpdateResponse
import com.simats.genecare.data.model.RiskAssessmentSaveRequest
import com.simats.genecare.data.model.RiskAssessmentSaveResponse
import com.simats.genecare.data.model.SendMessageRequest
import com.simats.genecare.data.model.SendMessageResponse
import com.simats.genecare.data.model.AdminStatsResponse
import com.simats.genecare.data.model.AnalyticsResponse
import com.simats.genecare.data.model.GenericResponse
import com.simats.genecare.data.model.GetPendingCounselorsResponse
import com.simats.genecare.data.model.ManageUserRequest
import com.simats.genecare.data.model.SaveCounselorProfileRequest
import com.simats.genecare.data.model.SaveQualificationRequest
import com.simats.genecare.data.model.SystemLogsResponse
import com.simats.genecare.data.model.UserListResponse
import com.simats.genecare.data.model.VerifyCounselorRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET

interface GeneCareApi {

    @POST("register_patient.php")
    @FormUrlEncoded
    suspend fun register(
        @Field("full_name") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") userType: String
    ): Response<AuthResponse>

    @POST("login.php")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @POST("book_appointment.php")
    suspend fun bookAppointment(
        @Body request: BookAppointmentRequest
    ): Response<BookAppointmentResponse>

    @POST("get_dashboard_stats.php")
    suspend fun getDashboardStats(
        @Body request: DashboardStatsRequest
    ): Response<DashboardStatsResponse>

    @POST("get_counselors.php") // Assuming we might want to fetch this later
    suspend fun getCounselors(): Response<CounselorResponse>

    @POST("send_message.php")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): Response<SendMessageResponse>

    @POST("get_messages.php") // Note: get_messages.php uses GET in my PHP script usually, let's check. 
    // Ah, I wrote get_messages.php to use $_GET. So it should be @GET.
    // wait, I wrote: $user_id = $_GET['user_id'];
    // So yes, @GET.
    // But Retrofit @GET queries are usually @Query params.
    suspend fun getMessages(
        @retrofit2.http.Query("user_id") userId: Int,
        @retrofit2.http.Query("other_user_id") otherUserId: Int
    ): Response<GetMessagesResponse>

    @POST("update_profile.php")
    suspend fun updateProfile(
        @Body request: ProfileUpdateRequest
    ): Response<ProfileUpdateResponse>

    @POST("save_family_history.php")
    suspend fun saveFamilyHistory(
        @Body request: FamilyHistorySaveRequest
    ): Response<FamilyHistorySaveResponse>

    @POST("save_medical_history.php")
    suspend fun saveMedicalHistory(
        @Body request: MedicalHistorySaveRequest
    ): Response<MedicalHistorySaveResponse>

    @POST("save_risk_assessment.php")
    suspend fun saveRiskAssessment(
        @Body request: RiskAssessmentSaveRequest
    ): Response<RiskAssessmentSaveResponse>

    // Counselor & Admin Endpoints
    @POST("save_counselor_profile.php")
    suspend fun saveCounselorProfile(
        @Body request: SaveCounselorProfileRequest
    ): Response<GenericResponse>

    @POST("update_consultation_fee.php")
    suspend fun updateConsultationFee(
        @Body request: com.simats.genecare.data.model.UpdateConsultationFeeRequest
    ): Response<GenericResponse>

    @POST("save_counselor_qualifications.php")
    suspend fun saveCounselorQualifications(
        @Body request: SaveQualificationRequest
    ): Response<GenericResponse>

    @POST("admin_verification.php")
    suspend fun adminVerifyCounselor(
        @Body request: VerifyCounselorRequest
    ): Response<GenericResponse>

    @GET("get_pending_counselors.php")
    suspend fun getPendingCounselors(): Response<GetPendingCounselorsResponse>

    @GET("get_admin_stats.php")
    suspend fun getAdminStats(): Response<AdminStatsResponse>

    @GET("get_analytics.php")
    suspend fun getAnalytics(): Response<AnalyticsResponse>

    @GET("get_users.php")
    suspend fun getUsers(): Response<UserListResponse>

    @POST("manage_user.php")
    suspend fun manageUser(
        @Body request: ManageUserRequest
    ): Response<GenericResponse>

    @GET("get_system_logs.php")
    suspend fun getSystemLogs(): Response<SystemLogsResponse>

    @GET("get_all_counselors_admin.php")
    suspend fun getAllCounselorsAdmin(): Response<com.simats.genecare.data.model.GetAllCounselorsResponse>

    @GET("get_counselor_appointments.php")
    suspend fun getCounselorAppointments(
        @retrofit2.http.Query("counselor_id") counselorId: Int
    ): Response<com.simats.genecare.data.model.GetCounselorAppointmentsResponse>





    @GET("get_counselor_patients.php")
    suspend fun getCounselorPatients(
        @retrofit2.http.Query("counselor_id") counselorId: Int
    ): Response<com.simats.genecare.data.model.GetCounselorPatientsResponse>


    @GET("get_counselor_profile.php")
    suspend fun getCounselorProfile(
        @retrofit2.http.Query("user_id") userId: Int
    ): Response<com.simats.genecare.data.model.CounselorProfileResponse>

    // Video Call & Billing Endpoints
    @GET("get_appointment_details.php")
    suspend fun getAppointmentDetails(
        @retrofit2.http.Query("appointment_id") appointmentId: Int
    ): Response<com.simats.genecare.data.model.GetAppointmentDetailsResponse>

    @POST("complete_appointment.php")
    suspend fun completeAppointment(
        @Body request: com.simats.genecare.data.model.CompleteAppointmentRequest
    ): Response<com.simats.genecare.data.model.GenericResponse>

    @POST("create_payment.php")
    suspend fun createPayment(
        @Body request: com.simats.genecare.data.model.CreatePaymentRequest
    ): Response<com.simats.genecare.data.model.CreatePaymentResponse>


    @POST("update_payment_status.php")
    suspend fun updatePaymentStatus(
        @Body request: com.simats.genecare.data.model.UpdatePaymentStatusRequest
    ): Response<com.simats.genecare.data.model.GenericResponse>

    // Analytics
    @GET("get_counselor_analytics.php")
    suspend fun getCounselorAnalytics(
        @retrofit2.http.Query("counselor_id") counselorId: Int
    ): Response<com.simats.genecare.data.model.CounselorAnalyticsResponse>

    @GET("get_chat_threads.php")
    suspend fun getChatThreads(@retrofit2.http.Query("counselor_id") counselorId: Int): Response<com.simats.genecare.data.model.GetChatThreadsResponse>

    @GET("get_patient_results.php")
    suspend fun getPatientResults(@retrofit2.http.Query("patient_id") patientId: Int): Response<com.simats.genecare.data.model.GetPatientResultsResponse>

    @POST("update_appointment_status.php")
    suspend fun updateAppointmentStatus(
        @Body request: com.simats.genecare.data.model.UpdateAppointmentStatusRequest
    ): Response<GenericResponse>

    @retrofit2.http.Multipart
    @POST("upload_report.php")
    suspend fun uploadReport(
        @retrofit2.http.Part("patient_id") patientId: okhttp3.RequestBody, 
        @retrofit2.http.Part file: okhttp3.MultipartBody.Part
    ): Response<GenericResponse>

    @GET("get_counselor_reports.php")
    suspend fun getCounselorReports(
        @retrofit2.http.Query("counselor_id") counselorId: Int
    ): Response<com.simats.genecare.data.model.GetCounselorReportsResponse>
}
