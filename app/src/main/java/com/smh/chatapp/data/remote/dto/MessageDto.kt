package com.smh.chatapp.data.remote.dto

import com.smh.chatapp.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class MessageDto(
    val id: String,
    val text: String,
    val username: String,
    val timestamp: Long
) {

    fun toMessage(): Message {
        val date = Date(timestamp)
        val formattedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(date)
        return Message(
            username = username,
            text = text,
            formattedTime = formattedDate
        )
    }

}
