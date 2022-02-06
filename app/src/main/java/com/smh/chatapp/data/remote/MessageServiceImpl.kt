package com.smh.chatapp.data.remote

import com.smh.chatapp.data.remote.dto.MessageDto
import com.smh.chatapp.domain.model.Message
import io.ktor.client.*
import io.ktor.client.request.*

class MessageServiceImpl(
    private val client: HttpClient
) : MessageService {
    override suspend fun getAllMessage(): List<Message> {
        return try {
            client.get<List<MessageDto>>(MessageService.EndPoints.GetAllMessage.url)
                .map { it.toMessage() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}