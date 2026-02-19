package com.simats.genecare.data.model

data class GetChatThreadsResponse(
    val status: String,
    val threads: List<ChatThread>
)

data class ChatThread(
    val id: String,
    val senderName: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarUrl: String?
)
