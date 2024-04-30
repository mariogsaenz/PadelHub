package com.example.padelhub.modelo

data class Partido(
    val id: String,
    val nombre: String,
    val fecha: String,
    val hora: String,
    val propietario: Usuario,
    val jugadores: List<Usuario>,
    val ubicacion: String,
    val estado: Boolean
)

