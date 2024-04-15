package com.example.padelhub.persistencia

import com.example.padelhub.modelo.Partido
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GestionPartido {
    suspend fun fetchPartidos(database: FirebaseFirestore): List<Partido> {
        val myList = mutableListOf<Partido>()

        try {
            val documents = database.collection("partido")
                .whereEqualTo("estado", true)
                .get()
                .await()

            for (document in documents) {
                val partido = Partido(
                    document.id,
                    document["nombre"].toString(),
                    document["fecha"].toString(),
                    document["hora"].toString(),
                    document["ubicacion"].toString(),
                    document["estado"] as Boolean
                )
                myList.add(partido)
            }
        } catch (e: Exception) {
            // Manejar errores
        }

        return myList
    }
}