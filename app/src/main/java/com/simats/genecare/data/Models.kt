package com.simats.genecare.data

data class Counselor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val initials: String,
    val colorHex: Long
)

data class TimeSlot(
    val time: String,
    val isAvailable: Boolean = true
)

data class Booking(
    val id: String,
    val counselorId: String,
    val date: String,
    val time: String,
    val timestamp: Long = System.currentTimeMillis()
)
