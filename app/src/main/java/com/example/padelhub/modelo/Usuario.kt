package com.example.padelhub.modelo

data class Usuario(
    var id: String = "",
    val nombre: String = "",
    val edad: Long = 0,
    val email: String = "",
    val partidosActivos: MutableList<String> = mutableListOf<String>(),
    val telefono: String = ""
)

