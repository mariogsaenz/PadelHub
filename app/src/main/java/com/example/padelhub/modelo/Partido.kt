package com.example.padelhub.modelo

data class Partido(
    var id: String = "",
    val nombre: String = "",
    val fecha: String = "",
    val hora: String = "",
    val propietario: Usuario? = null,
    val jugadores: MutableList<Usuario> = mutableListOf<Usuario>(),
    val ubicacion: String = "",
    val estado: Boolean = false,
){
    fun Partido(){}
}

