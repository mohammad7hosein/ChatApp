package com.smh.chatapp.data.remote

import com.smh.chatapp.domain.model.Message

interface MessageService {

    suspend fun getAllMessage(): List<Message>

    companion object {
        const val BASE_URL = "http://192.168.1.4:8080"
    }

    sealed class EndPoints(val url: String) {
        object GetAllMessage: EndPoints("$BASE_URL/messages")
    }

}