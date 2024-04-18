package com.example.padelhub.persistencia

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
            // Manejar errores
        }

        return myList
    }

}