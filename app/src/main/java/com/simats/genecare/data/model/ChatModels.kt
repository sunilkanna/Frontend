package com.simats.genecare.data.model

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("message_text") val messageText: String,
    @SerializedName("sent_at") val sentAt: String,
    @SerializedName("is_read") val isRead: Boolean
)

data class SendMessageRequest(
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("message_text") val messageText: String
)

data class SendMessageResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message_id") val messageId: Int?
)

data class GetMessagesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("messages") val messages: List<ChatMessageDto>
)
