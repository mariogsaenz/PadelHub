package com.example.padelhub.modelo

import java.util.Date

data class ChatMessage(
    val message: String= "",
    val senderId: String= "",
    val timestamp: Date? = null
)
