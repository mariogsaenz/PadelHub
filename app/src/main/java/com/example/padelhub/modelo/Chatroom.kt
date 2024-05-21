package com.example.padelhub.modelo

import java.sql.Timestamp

data class Chatroom(
    val chatroomId: String = "",
    val userIds: MutableList<String> = mutableListOf<String>(),
    val lastMessageTimestamp: Timestamp? = null,
    val lastMessageSenderId: String = "",
    val lastMessage: String = ""
)
