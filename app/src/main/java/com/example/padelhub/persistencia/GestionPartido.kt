package com.example.padelhub.persistencia

import android.util.Log
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Suppress("UNCHECKED_CAST")
class GestionPartido {
    suspend fun fetchPartidos(database: FirebaseFirestore): List<Partido> {
        val myList = mutableListOf<Partido>()

        try {
            val documents = database.collection("partido")
                .whereEqualTo("estado", true)
                .get()
                .await()
            Log.d("document: ", documents.toString())
            for (document in documents) {
                Log.d("document: ", document.data.toString())
                val partido = Partido(
                    document.id,
                    document["nombre"].toString(),
                    document["fecha"].toString(),
                    document["hora"].toString(),
                    document["jugadores"] as List<Usuario>,
                    document["ubicacion"].toString(),
                    document["estado"] as Boolean
                )
                Log.d("Lista: ", partido.toString())
                myList.add(partido)
            }
        } catch (e: Exception) {
            // Manejar errores
        }

        return myList
    }
}

