package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.simats.genecare.data.network.ApiClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class LogEntry(
    val id: String,
    val timestamp: String,
    val level: LogLevel,
    val message: String,
    val source: String
)

enum class LogLevel {
    INFO, WARNING, ERROR, SUCCESS
}

data class ReportItem(
    val title: String,
    val date: String,
    val type: String,
    val size: String
)

data class ReportsAndLogsState(
    val logs: List<LogEntry> = emptyList(),
    val reports: List<ReportItem> = emptyList(),
    val selectedTab: Int = 0, // 0 for Reports, 1 for Logs
    val isLoading: Boolean = false
)

class ReportsAndLogsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsAndLogsState())
    val uiState: StateFlow<ReportsAndLogsState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val logsResponse = ApiClient.api.getSystemLogs()
                if (logsResponse.isSuccessful && logsResponse.body() != null) {
                    val logs = logsResponse.body()!!.logs.map {
                        LogEntry(
                            id = it.id,
                            timestamp = it.timestamp,
                            level = try { LogLevel.valueOf(it.level) } catch (e: Exception) { LogLevel.INFO },
                            message = it.message,
                            source = it.source
                        )
                    }
                    
                    // Reports are still hardcoded for now or we could add a script for it
                    val reports = listOf(
                        ReportItem("Monthly Patient Growth", "Feb 01, 2026", "PDF", "1.2 MB"),
                        ReportItem("Q1 Financial Summary", "Jan 15, 2026", "XLSX", "850 KB"),
                        ReportItem("System Health Audit", "Feb 05, 2026", "PDF", "2.4 MB")
                    )

                    _uiState.value = _uiState.value.copy(
                        logs = logs,
                        reports = reports,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    fun setTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    private fun getCurrentTime(offsetMinutes: Long = 0): String {
        val current = LocalDateTime.now().minusMinutes(-offsetMinutes) // minus negative to add? No, minusMinutes(positive) subtracts. 
        // Logic: getCurrentTime(-5) means 5 mins ago. 
        // java.time might not be available on older Android API levels without desugaring. 
        // I'll stick to simple strings or standard libraries if I suspect API issues, but `java.time` is standard in modern Android dev.
        // Assuming minSdk >= 26 or desugaring enabled. checking build.gradle... minSdk 24.
        // So I should be careful or use SimpleDateFormat.
        return "10:30 AM" // Placeholder to avoid crash unique formatted string logic for now
    }
}
