package com.simats.genecare.data.model

data class GetCounselorReportsResponse(
    val status: String,
    val reports: List<CounselorReportItem>
)

data class CounselorReportItem(
    val id: String,
    val title: String, // mapped from fileName
    val category: String = "General", // Placeholder or determine from extensione
    val date: String,
    val patientName: String,
    val patientId: Int,
    val fileUrl: String
)
