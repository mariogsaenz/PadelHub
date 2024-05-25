package com.example.padelhub.persistencia

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.example.padelhub.modelo.ChatMessage
import com.example.padelhub.modelo.Chatroom
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Usuario
import com.example.padelhub.ui.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.Date

@Suppress("UNCHECKED_CAST")
class GestionChat {
    suspend fun fetch(database: FirebaseFirestore, auth: FirebaseAuth): List<Chatroom> {
        val usuarioActual = GestionUsuario().getUsuarioActual(auth,database)
        val myList = mutableListOf<Chatroom>()
        val usuarioId = usuarioActual?.id

        try {
            if(usuarioActual!=null) {
                for(chatid in usuarioActual.chatrooms){
                    val querySnapshot = database.collection("chatroom")
                        .document(chatid)
                        .get()
                        .await()
                    val chatroom = querySnapshot.toObject(Chatroom::class.java)
                    if (chatroom != null) {
                        chatroom.chatroomId=chatid.toString()
                    }
                    if (chatroom != null) {
                        myList.add(chatroom)
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.d("Error al recuperar los partidos creados por el usuario activo: ", e.message.toString())
        }
        Log.d("Lista de partidos del usuario activo: ", myList.toString())
        return myList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun enviarMensaje(chatroomId: String, mensaje: String, database: FirebaseFirestore, auth: FirebaseAuth){
        val usuarioActual = GestionUsuario().getUsuarioActual(auth,database)

        val mensajeEnviado = usuarioActual?.let { ChatMessage(mensaje, it.id, Date()) }

        try {
            val documentoRef = database.collection("chatroom")
                .document(chatroomId)
            val documentoChat = documentoRef.get().await()
            val chatroom = documentoChat.toObject(Chatroom::class.java)
            if (chatroom != null) {
                if (mensajeEnviado != null) {
                    chatroom.messages.add(mensajeEnviado)
                    database.runTransaction { transaction ->
                        transaction.update(documentoRef, "messages", chatroom.messages)
                    }.await()
                }
            }

        }
        catch (e: Exception) {
            Log.d("Error al recuperar los partidos creados por el usuario activo: ", e.message.toString())
        }
    }
}
