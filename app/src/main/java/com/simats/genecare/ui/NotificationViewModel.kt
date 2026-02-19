package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel : ViewModel() {

    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                "1",
                "Counselor Verification Pending",
                "3 new counselors awaiting verification",
                "1h ago",
                NotificationType.VERIFICATION_PENDING,
                isRead = false
            ),
            NotificationItem(
                "2",
                "System Alert",
                "Server usage at 85% - monitoring required",
                "2h ago",
                NotificationType.SYSTEM_ALERT,
                isRead = false
            ),
            NotificationItem(
                "3",
                "New User Registrations",
                "25 new patients registered today",
                "4h ago",
                NotificationType.NEW_USER,
                isRead = false
            ),
            NotificationItem(
                "4",
                "Verification Completed",
                "Dr. Sarah Johnson has been verified",
                "6h ago",
                NotificationType.VERIFICATION_COMPLETE,
                isRead = false
            ),
            NotificationItem(
                "5",
                "Backup Completed",
                "Daily system backup completed successfully",
                "1d ago",
                NotificationType.BACKUP,
                isRead = false
            ),
            // Counselor Specific Notifications
            NotificationItem(
                "6",
                "Upcoming Session",
                "Video consultation with Emily Rodriguez in 15 mins",
                "15m ago",
                NotificationType.SESSION_REMINDER,
                isRead = false
            ),
            NotificationItem(
                "7",
                "New Message",
                "You have a new message from Michael Chen",
                "30m ago",
                NotificationType.MESSAGE,
                isRead = false
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun markAllAsRead() {
        val updatedList = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = updatedList
    }
}
