package com.example.padelhub.modelo

import java.sql.Timestamp

data class ChatMessage(
    val message: String= "",
    val senderid: String= "",
    val timestamp: Timestamp? = null
)
