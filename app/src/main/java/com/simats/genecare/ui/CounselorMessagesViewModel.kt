package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MessageThread(
    val id: String,
    val senderName: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarColor: Long
)

class CounselorMessagesViewModel : ViewModel() {
    private val _threads = MutableStateFlow<List<MessageThread>>(emptyList())
    val threads: StateFlow<List<MessageThread>> = _threads.asStateFlow()

    init {
        loadThreads()
    }

    fun loadThreads() {
        val user = com.simats.genecare.data.UserSession.getUser() ?: return
        viewModelScope.launch {
            try {
                val response = com.simats.genecare.data.network.ApiClient.api.getChatThreads(user.id)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val threads = response.body()?.threads?.map { thread ->
                        MessageThread(
                            id = thread.id,
                            senderName = thread.senderName,
                            lastMessage = thread.lastMessage,
                            time = thread.time,
                            unreadCount = thread.unreadCount,
                            avatarColor = 0xFF00ACC1 // Placeholder color, or map from avatarUrl
                        )
                    } ?: emptyList()
                    _threads.value = threads
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
