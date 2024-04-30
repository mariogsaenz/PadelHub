package com.example.padelhub.modelo

data class Usuario(
    val id: String,
    val nombre: String,
    val edad: Long,
    val email: String,
    val partidosActivos: List<String>
)

