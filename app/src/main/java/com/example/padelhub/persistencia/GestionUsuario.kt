package com.example.padelhub.persistencia

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.padelhub.MainActivity
import com.example.padelhub.ui.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

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
                    val usuario = hashMapOf(
                        "email" to email,
                        "nombre" to nombre,
                        "edad" to edad
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
}

