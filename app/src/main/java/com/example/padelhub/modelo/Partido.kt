package com.example.padelhub.modelo

data class Partido(
    val id: String,
    val nombre: String,
    val fecha: String,
    val hora: String,
    val ubicacion: String,
    val estado: Boolean
)
