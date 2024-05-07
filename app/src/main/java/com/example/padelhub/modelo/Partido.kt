package com.example.padelhub.modelo

data class Partido(
    var id: String = "",
    val nombre: String = "",
    val fecha: String = "",
    val hora: String = "",
    val propietario: Usuario? = null,
    val jugadores: List<Usuario> = listOf(),
    val ubicacion: String = "",
    val estado: Boolean = false,
){
    fun Partido(){}
}

