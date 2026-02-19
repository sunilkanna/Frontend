package com.simats.genecare.data.repository

import com.simats.genecare.data.model.ChatMessageDto
import com.simats.genecare.data.model.GetMessagesResponse
import com.simats.genecare.data.model.SendMessageRequest
import com.simats.genecare.data.model.SendMessageResponse
import com.simats.genecare.data.network.ApiClient
import retrofit2.Response

class ChatRepository {
    private val api = ApiClient.api

    suspend fun getMessages(userId: Int, otherUserId: Int): Response<GetMessagesResponse> {
        return api.getMessages(userId, otherUserId)
    }

    suspend fun sendMessage(senderId: Int, receiverId: Int, messageText: String): Response<SendMessageResponse> {
        return api.sendMessage(SendMessageRequest(senderId, receiverId, messageText))
    }
}
