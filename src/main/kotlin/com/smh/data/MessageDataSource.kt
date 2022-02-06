package com.smh.data

import com.smh.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessage() : List<Message>

    suspend fun insertMessage(message: Message)

}