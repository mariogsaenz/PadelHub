package com.example.padelhub.persistencia

import android.util.Log
import androidx.navigation.NavController
import com.example.padelhub.modelo.ChatMessage
import com.example.padelhub.modelo.Chatroom
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
            Log.d("Error al buscar partidos: ", e.message.toString())
        }

        return myList
    }

    suspend fun getPartido(id: String,database: FirebaseFirestore): Partido {
        var partido = Partido()

        try {
            val document = database.collection("partido").document(id).get().await()
            partido = document.toObject(Partido::class.java)!!
            partido.id=document.id

        } catch (e: Exception) {
            Log.d("Error al buscar partidos: ", e.message.toString())
        }

        return partido
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
            Log.d("Error al buscar partidos con el nombre indicado: ", e.message.toString())
        }

        return myList2
    }


    suspend fun crear(fecha: String,hora: String,ubicacion: String, nombre:String, database: FirebaseFirestore, auth: FirebaseAuth) {
        val usuario: Usuario?
        runBlocking {
            usuario = GestionUsuario().getUsuarioActual(auth, database)
        }
        val lista = mutableListOf<String>()
        usuario?.let { lista.add(it.id) }
        val partido = hashMapOf(
            "nombre" to nombre,
            "fecha" to fecha,
            "hora" to hora,
            "propietario" to (usuario?.id ?: ""),
            "jugadores" to lista,
            "ubicacion" to ubicacion,
            "estado" to true
        )
        database.collection("partido").add(partido)
            .addOnSuccessListener { it2 ->
                usuario?.let {
                    var usuarioDB = database.collection("usuario").document(usuario.id)
                    database.runTransaction { transaction ->
                        usuario.partidosActivos.add(it2.id)
                        transaction.update(usuarioDB, "partidosActivos", usuario.partidosActivos)
                        //Crear chat room
                        val mensajes = mutableListOf<ChatMessage>()
                        val participantes = mutableListOf<String>(usuario.id)
                        val chatroom = hashMapOf(
                            "partido" to it2.id,
                            "userIds" to participantes,
                            "messages" to mensajes,
                        )
                        database.collection("chatroom").add(chatroom)
                            .addOnSuccessListener { newChat ->
                                database.runTransaction { transaction ->
                                    usuario.chatrooms.add(newChat.id)
                                    transaction.update(usuarioDB, "chatrooms", usuario.chatrooms)
                                }
                                // Success
                                null

                            }
                    }.addOnSuccessListener { Log.d("Transacción", "Transaction success!") }
                        .addOnFailureListener { e ->
                            Log.w(
                                "Transacción",
                                "Transaction failure.",
                                e
                            )
                        }
                }
            }.addOnFailureListener { e -> Log.w("Partido", "Error writing document", e) }
    }
     suspend fun changeEstadoToAcabado(partido: Partido, database: FirebaseFirestore){
        val sfDocRef = database.collection("partido").document(partido.id)

        database.runTransaction { transaction ->

            transaction.update(sfDocRef, "estado", false)

            //AQUÍ HABRÍA QUE HACER
            //Recorrer la lista de jugadores
            //Para cada jugador, meternos en su lista de partidos activos
            //y eliminar el partido cuyo id coincida con el partido que estamos eliminando

            // Success
            null
        }.addOnSuccessListener { Log.d("Transacción", "Transaction success!") }
            .addOnFailureListener { e -> Log.w("Transacción", "Transaction failure.", e) }
    }

    suspend fun unirmeAPartido(partido: Partido, database: FirebaseFirestore, auth: FirebaseAuth, navController: NavController){

        val sfDocRef = database.collection("partido").document(partido.id)
        var chatRoom = Chatroom()
        val usuario: Usuario?
        runBlocking {
            usuario= GestionUsuario().getUsuarioActual(auth, database)
        }
        usuario?.let {
            val docChatRooms = database.collection("chatroom")
                .whereEqualTo("partido",partido.id)
                .get()
                .await()
            for(document in docChatRooms.documents){
                chatRoom= document.toObject<Chatroom>(Chatroom::class.java)!!
                chatRoom.chatroomId=document.id
                chatRoom.userIds.add(usuario.id)
                val chatRef= database.collection("chatroom").document(chatRoom.chatroomId)
                database.runTransaction { addUserChat->
                    addUserChat.update(chatRef,"userIds", chatRoom.userIds)
                }
            }
            database.runTransaction { transaction ->
                //AQUÍ DEBERÍA MANDAR MENSAJE DE SOLICITUD AL CHAT DEL PARTIDO
                val snapshot = transaction.get(sfDocRef)
                val jugadores = snapshot.get("jugadores") as? MutableList<String> ?: mutableListOf()
                if (!jugadores.contains(usuario.id)) {
                    jugadores.add(usuario.id)
                    transaction.update(sfDocRef, "jugadores", jugadores)
                }
                // Success
                null
            }.addOnSuccessListener{
                    var usuarioDB = database.collection("usuario").document(usuario.id)
                    database.runTransaction { transaction ->
                        usuario.partidosActivos.add(partido.id)
                        usuario.chatrooms.add(chatRoom.chatroomId)
                        transaction.update(usuarioDB, "partidosActivos", usuario.partidosActivos)
                        transaction.update(usuarioDB, "chatrooms", usuario.chatrooms)
                        // Success
                        null
                    }
            }.addOnFailureListener { e -> Log.w("Partido", "Error", e) }
        }

        actualizarInterfaz(navController)
    }

    suspend fun desapuntarmeDePartido(partido: Partido, database: FirebaseFirestore, auth: FirebaseAuth, navController: NavController){

        val sfDocRef = database.collection("partido").document(partido.id)
        var chatRoom = Chatroom()
        val usuario: Usuario?
        runBlocking {
            usuario= GestionUsuario().getUsuarioActual(auth, database)
        }
        usuario?.let {
            val docChatRooms = database.collection("chatroom")
                .whereEqualTo("partido",partido.id)
                .get()
                .await()
            for(document in docChatRooms.documents){
                chatRoom= document.toObject<Chatroom>(Chatroom::class.java)!!
                chatRoom.chatroomId=document.id
                if(chatRoom.userIds.contains(usuario.id)) {
                    chatRoom.userIds.remove(usuario.id)
                    val chatRef= database.collection("chatroom").document(chatRoom.chatroomId)
                    database.runTransaction { addUserChat->
                        addUserChat.update(chatRef,"userIds", chatRoom.userIds)
                    }
                }

            }
            database.runTransaction { transaction ->


                //AQUÍ DEBERÍA MANDAR MENSAJE AL CHAT DEL PARTIDO INFORMANDO QUE SE HA DESAPUNTADO

                val snapshot = transaction.get(sfDocRef)
                val jugadores = snapshot.get("jugadores") as? MutableList<String> ?: mutableListOf()
                if (jugadores.contains(usuario.id)) {
                    jugadores.remove(usuario.id)
                    transaction.update(sfDocRef, "jugadores", jugadores)
                }



                // Success
                null
            }.addOnSuccessListener{
                var usuarioDB = database.collection("usuario").document(usuario.id)
                database.runTransaction { transaction ->
                    usuario.partidosActivos.remove(partido.id)
                    usuario.chatrooms.remove(chatRoom.chatroomId)
                    transaction.update(usuarioDB, "partidosActivos", usuario.partidosActivos)
                    transaction.update(usuarioDB, "chatrooms", usuario.chatrooms)
                    android.util.Log.d("Transacción", "Transaction success!")
                    // Success
                    null
                }
            }.addOnFailureListener { e -> Log.w("Partido", "Error", e) }

        }
        actualizarInterfaz(navController)
    }

    suspend fun estaApuntado(partido: Partido, database: FirebaseFirestore, auth: FirebaseAuth): Boolean {

        val sfDocRef = database.collection("partido").document(partido.id)

        val usuario: Usuario?
        runBlocking {
            usuario= GestionUsuario().getUsuarioActual(auth, database)
        }
        var boolean: Boolean = false
        usuario?.let {
            database.runTransaction { transaction ->

                val snapshot = transaction.get(sfDocRef)
                val jugadores = snapshot.get("jugadores") as? MutableList<String> ?: mutableListOf()
                boolean = jugadores.contains(usuario.id)
            }.await()
        }

        return boolean
    }

    suspend fun actualizarInterfaz(navController: NavController){
        navController.navigate("pantalla_inicio")
    }

}


