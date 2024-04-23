package com.example.padelhub.modelo

data class Partido(
    val id: String,
    val nombre: String,
    val fecha: String,
    val hora: String,
    val jugadores: List<Usuario>,
    val ubicacion: String,
    val estado: Boolean
)

