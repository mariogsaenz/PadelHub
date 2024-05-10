package com.example.padelhub.persistencia

import android.util.Log
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Pista
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GestionPista {

    suspend fun fetchPistas(database: FirebaseFirestore): List<Pista> {
        val myList = mutableListOf<Pista>()

        try {
            val documents = database.collection("pista")
                //.whereEqualTo("estado", true)
                .get()
                .await()

            for (document in documents) {
                val pista = Pista(
                    document.id,
                    document["nombre"].toString(),
                    document["ubicacion"].toString(),
                    document["numeroPistas"].toString(),
                    document["precio"].toString(),
                )
                myList.add(pista)
            }
        } catch (e: Exception) {
            Log.d("Error al buscar pistas: ", e.message.toString())
        }

        return myList
    }

    suspend fun fetchPistasNombre(filtroBusqueda: String ,database: FirebaseFirestore): List<Pista> {
        val myList2 = mutableListOf<Pista>()

        try {
            val documents = database.collection("pista")
                //.whereEqualTo("estado", true)
                .get()
                .await()

            for (document in documents) {
                /*val pista = Pista(
                    document.id,
                    document["nombre"].toString(),
                    document["ubicacion"].toString(),
                    document["numeroPistas"].toString(),
                    document["precio"].toString(),
                )*/
                val pista = document.toObject(Pista::class.java)
                pista?.let{
                    if(it.nombre.contains(filtroBusqueda, ignoreCase = true)){
                        it.id = document.id
                        myList2.add(pista)
                    }
                }

            }
        } catch (e: Exception) {
            Log.d("Error al buscar una pista con el nombre indicado : ", e.message.toString())
        }

        return myList2
    }

}

