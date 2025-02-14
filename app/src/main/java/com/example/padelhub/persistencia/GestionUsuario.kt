package com.example.padelhub.persistencia

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.padelhub.MainActivity
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Usuario
import com.example.padelhub.ui.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GestionUsuario {
    internal fun createAccount(auth: FirebaseAuth, database: FirebaseFirestore, nombre: String, edad: Int, email: String, password: String, navController: NavController, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MainActivity.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(
                        context,
                        "Te has registrado con exito.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val partidos = listOf<String>()
                    val chats = listOf<String>()
                    val usuario = hashMapOf(
                        "email" to email,
                        "nombre" to nombre,
                        "edad" to edad,
                        "partidosActivos" to partidos,
                        "chatrooms" to chats
                    )
                    database.collection("usuario").add(usuario)
                        .addOnSuccessListener { Log.d(MainActivity.TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(MainActivity.TAG, "Error writing document", e) }
                    navController.navigate(route = AppScreens.HomeScreen_Inicio.route)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MainActivity.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }


        // [END create_user_with_email]
    }

    internal fun signIn(auth: FirebaseAuth, email: String, password: String, navController: NavController, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MainActivity.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(
                        context,
                        "Te has logueado con exito.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    navController.navigate(route = AppScreens.HomeScreen_Inicio.route)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MainActivity.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }


        // [END sign_in_with_email]
    }

    suspend fun getUsuarioActual(auth: FirebaseAuth, database: FirebaseFirestore): Usuario? {

        val currentUser = auth.currentUser
        val emailUser = currentUser?.email
        val usuarios = database.collection("usuario")
            .whereEqualTo("email", emailUser).get().await()
        var user: Usuario? = null

        for (usuario in usuarios){
            user = usuario.toObject(Usuario::class.java)
            user.id=usuario.id
        }

        return user
    }

    internal fun signOut(auth: FirebaseAuth, navController: NavController, context: Context) {
        auth.signOut()
        Toast.makeText(
            context,
            "Te has deslogueado con exito.",
            Toast.LENGTH_SHORT,
        ).show()
        navController.navigate(route = AppScreens.HomeScreen_Login.route)
        // [END sign_in_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    suspend fun changeDatosUsuario(auth: FirebaseAuth, database: FirebaseFirestore, nombre: String, edad: Int){
        val doc = getUsuarioActual(auth,database)?.let { database.collection("usuario").document(it.id) }

        database.runTransaction { transaction ->

            if (doc != null) {
                transaction.update(doc, "nombre", nombre)
            }
            if (doc != null) {
                transaction.update(doc, "edad", edad)
            }

            // Success
            null
        }.addOnSuccessListener { Log.d("Transacción", "Transaction success!") }
            .addOnFailureListener { e -> Log.w("Transacción", "Transaction failure.", e) }
    }

    suspend fun getPartidosPropietario(auth: FirebaseAuth, database: FirebaseFirestore): MutableList<Partido> {
        val usuarioActual = getUsuarioActual(auth,database)?.let { database.collection("usuario").document(it.id) }
        val myList = mutableListOf<Partido>()
        val usuarioId = usuarioActual?.id

        try {
            val querySnapshot = database.collection("partido")
                .whereEqualTo("estado", true)
                .whereEqualTo("propietario",usuarioId)
                .get()
                .await()
            Log.d("Coleccion de partidos del usuario activo: ", querySnapshot.toString())

            for (document in querySnapshot.documents) {
                val partido = document.toObject(Partido::class.java)
                if (partido != null) {
                    partido.id=document.id
                    myList.add(partido)
                }
            }
        }
        catch (e: Exception) {
            Log.d("Error al recuperar los partidos creados por el usuario activo: ", e.message.toString())
        }
        Log.d("Lista de partidos del usuario activo: ", myList.toString())
        return myList

    }
}


