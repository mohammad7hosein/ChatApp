package com.smh.room

import com.smh.data.MessageDataSource
import com.smh.data.model.Message
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(username))
            throw MemberAlreadyExistException()
        members[username] = Member(
            username = username,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun sendMessage(senderUsername: String, message: String) {
        members.values.forEach { member ->
            val messageEntity = Message(
                username = senderUsername,
                text = message,
                timestamp = System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)

            val parseMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parseMessage))
        }
    }

    suspend fun getAllMessage(): List<Message> {
        return messageDataSource.getAllMessage()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if (members.containsKey(username))
            members.remove(username)
    }
}