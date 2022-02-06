package com.smh.chatapp.presentation.chat

import com.smh.chatapp.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)
