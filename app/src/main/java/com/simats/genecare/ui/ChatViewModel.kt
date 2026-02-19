package com.simats.genecare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.genecare.data.UserSession
import com.simats.genecare.data.model.ChatMessageDto
import com.simats.genecare.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.lifecycle.ViewModelProvider

class ChatViewModel(private val otherUserId: Int) : ViewModel() {
    private val repository = ChatRepository()

    private val _messages = MutableStateFlow<List<ChatMessageDto>>(emptyList())
    val messages: StateFlow<List<ChatMessageDto>> = _messages

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                fetchMessages()
                delay(3000) // Poll every 3 seconds
            }
        }
    }

    private fun fetchMessages() {
        val userId = UserSession.getUserId() ?: return
        viewModelScope.launch {
            try {
                val response = repository.getMessages(userId, otherUserId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _messages.value = response.body()?.messages ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(text: String) {
        val userId = UserSession.getUserId() ?: return
        
        viewModelScope.launch {
            try {
                val response = repository.sendMessage(userId, otherUserId, text)
                if (response.isSuccessful && response.body()?.status == "success") {
                    fetchMessages() // Refresh immediately
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class Factory(private val otherUserId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(otherUserId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
