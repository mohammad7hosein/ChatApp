package com.smh.chatapp.domain.model

data class Message(
    val username: String,
    val text: String,
    val formattedTime: String
)
