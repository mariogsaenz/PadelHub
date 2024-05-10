package com.example.padelhub.persistencia

import android.util.Log
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

@Suppress("UNCHECKED_CAST")
class GestionPartido {
    suspend fun fetch(database: FirebaseFirestore): List<Partido> {
        val myList = mutableListOf<Partido>()

        try {
            val documents = database.collection("partido")
                .whereEqualTo("estado", true)
                .get()
                .await()
            Log.d("document: ", documents.toString())
            for(document in documents) {
                val partido = document.toObject(Partido::class.java)
                partido.id=document.id
                Log.d("partidfo: ", partido.toString())
                myList.add(partido)
                Log.d("LA LISTA1: ", myList.toString())
            }
        } catch (e: Exception) {
            Log.d("Partido: ", e.message.toString())
        }

        return myList
    }

    suspend fun fetchNombre(filtroBusqueda: String, database: FirebaseFirestore): List<Partido> {
        val myList2 = mutableListOf<Partido>()

        try {
            val querySnapshot = database.collection("partido")
                .whereEqualTo("estado", true)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val partido = document.toObject(Partido::class.java)
                partido?.let {
                    if (it.nombre.contains(filtroBusqueda, ignoreCase = true)) {
                        it.id = document.id
                        myList2.add(it)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("Error al buscar partidos: ", e.message.toString())
        }

        return myList2
    }


    suspend fun crear(fecha: String,hora: String,ubicacion: String, nombre:String,
                             database: FirebaseFirestore, auth: FirebaseAuth){
        val usuario: Usuario?
        runBlocking {
            usuario= GestionUsuario().getUsuarioActual(auth, database)
        }
        val lista = mutableListOf<Usuario>()
        usuario?.let { lista.add(it) }
        val partido = hashMapOf(
            "nombre" to nombre,
            "fecha" to fecha,
            "hora" to hora,
            "propietario" to usuario,
            "jugadores" to lista,
            "ubicacion" to ubicacion,
            "estado" to true
        )
        database.collection("partido").add(partido)
            .addOnSuccessListener {it->
                usuario?.let {
                    var usuarioDB = database.collection("usuario").document(usuario.id)
                    database.runTransaction { transaction ->
                        usuario.partidosActivos.add(it.id)
                        transaction.update(usuarioDB, "partidosActivos", usuario.partidosActivos)

                        // Success
                        null

                    }.addOnSuccessListener { Log.d("Transacción", "Transaction success!") }
                    .addOnFailureListener { e -> Log.w("Transacción", "Transaction failure.", e) }
                }
            }
            .addOnFailureListener { e -> Log.w("Partido", "Error writing document", e) }
    }
    suspend fun changeEstadoToAcabado(partido: Partido, database: FirebaseFirestore){
        val sfDocRef = database.collection("partido").document(partido.id)

        database.runTransaction { transaction ->

            transaction.update(sfDocRef, "estado", false)

            // Success
            null
        }.addOnSuccessListener { Log.d("Transacción", "Transaction success!") }
            .addOnFailureListener { e -> Log.w("Transacción", "Transaction failure.", e) }
    }
}


