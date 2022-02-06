package com.smh.chatapp.data.remote

import com.smh.chatapp.domain.model.Message
import com.smh.chatapp.util.ResponseModel
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(username: String): ResponseModel<Unit>

    suspend fun closeSession()

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    companion object {
        const val BASE_URL = "ws://192.168.1.4:8080"
    }

    sealed class EndPoints(val url: String) {
        object ChatSocket: EndPoints("$BASE_URL/chat-socket")
    }

}