package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.BookingRepository
import com.simats.genecare.data.Counselor
import com.simats.genecare.data.TimeSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingUiState(
    val counselors: List<Counselor> = emptyList(),
    val timeSlots: List<TimeSlot> = emptyList(),
    val selectedCounselor: Counselor? = null,
    val selectedDate: String = "1", // Default to 1st
    val selectedTime: String? = "10:30 AM", // Default from UI design
    val isBookingConfirmed: Boolean = false,
    val isSessionApproved: Boolean = false, 
    val uploadedReportName: String? = null,
    val uploadedReportUri: String? = null,
    // Dynamic Date Handling
    val currentYear: Int = 0,
    val currentMonth: Int = 0,
    val monthName: String = "",
    val daysInMonth: List<String> = emptyList()
)

class BookingViewModel(
    private val repository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        // Initialize with actual current date
        val cal = java.util.Calendar.getInstance()
        val year = cal.get(java.util.Calendar.YEAR)
        val month = cal.get(java.util.Calendar.MONTH)
        updateDateState(year, month)

        viewModelScope.launch {
            val counselorList = repository.fetchCounselors()
            _uiState.value = _uiState.value.copy(
                counselors = counselorList,
                timeSlots = repository.getTimeSlots(),
                selectedCounselor = counselorList.firstOrNull()
            )
        }
    }

    private fun updateDateState(year: Int, month: Int) {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.YEAR, year)
        cal.set(java.util.Calendar.MONTH, month)
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1)

        val maxDays = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        val days = (1..maxDays).map { it.toString() }
        val monthName = cal.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, java.util.Locale.US) ?: "January"

        _uiState.value = _uiState.value.copy(
            currentYear = year,
            currentMonth = month,
            monthName = monthName,
            daysInMonth = days
        )
    }

    fun nextMonth() {
        var year = _uiState.value.currentYear
        var month = _uiState.value.currentMonth + 1
        if (month > 11) {
            month = 0
            year++
        }
        updateDateState(year, month)
    }

    fun previousMonth() {
        var year = _uiState.value.currentYear
        var month = _uiState.value.currentMonth - 1
        if (month < 0) {
            month = 11
            year--
        }
        updateDateState(year, month)
    }

    fun selectCounselor(counselor: Counselor) {
        _uiState.value = _uiState.value.copy(selectedCounselor = counselor)
    }

    fun selectDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun selectTime(time: String) {
        _uiState.value = _uiState.value.copy(selectedTime = time)
    }

    fun confirmBooking() {
        val state = _uiState.value
        if (state.selectedCounselor != null && state.selectedTime != null) {
            viewModelScope.launch {
                repository.bookSession(
                    counselorId = state.selectedCounselor.id,
                    date = "${state.monthName} ${state.selectedDate}, ${state.currentYear}",
                    time = state.selectedTime
                )
                _uiState.value = _uiState.value.copy(isBookingConfirmed = true)
            }
        }
    }

    fun onReportSelected(uri: String, name: String) {
        _uiState.value = _uiState.value.copy(
            uploadedReportUri = uri,
            uploadedReportName = name
        )
    }

    fun removeReport() {
        _uiState.value = _uiState.value.copy(
            uploadedReportUri = null,
            uploadedReportName = null
        )
    }


    fun resetBookingState() {
        val cal = java.util.Calendar.getInstance()
        val year = cal.get(java.util.Calendar.YEAR)
        val month = cal.get(java.util.Calendar.MONTH)
        // Reset to initial state but fetch counselors
        viewModelScope.launch {
            val counselorList = repository.fetchCounselors()
            
            // Create base state
            _uiState.value = BookingUiState(
                 counselors = counselorList,
                 timeSlots = repository.getTimeSlots(),
                 selectedCounselor = counselorList.firstOrNull()
            )
            // Apply date logic
            updateDateState(year, month)
        }
    }
}
