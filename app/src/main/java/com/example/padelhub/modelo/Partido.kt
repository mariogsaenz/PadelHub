package com.example.padelhub.modelo

data class Partido(
    var id: String = "",
    val nombre: String = "",
    val fecha: String = "",
    val hora: String = "",
    val propietario: String = "",
    val jugadores: MutableList<String> = mutableListOf<String>(),
    val ubicacion: String = "",
    val estado: Boolean = false,
){
    fun Partido(){}
}

