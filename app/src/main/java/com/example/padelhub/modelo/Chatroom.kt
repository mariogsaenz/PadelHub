package com.example.padelhub.modelo

import java.sql.Timestamp

data class Chatroom(
    var chatroomId: String = "",
    val userIds: MutableList<String> = mutableListOf<String>(),
    val messages: MutableList<ChatMessage> = mutableListOf<ChatMessage>()
)
