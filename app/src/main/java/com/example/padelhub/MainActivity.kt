package com.example.padelhub

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.padelhub.ui.navigation.AppNavigation
import com.example.padelhub.ui.navigation.AppScreens
import com.example.padelhub.ui.theme.PadelHubTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {

    //[START declare_auth]
    private lateinit var auth: FirebaseAuth
    //[END declare_auth]

    //[START declare_auth]
    private lateinit var database: FirebaseFirestore
    //[END declare_auth]

    private lateinit var context: Context
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // [START initialize_database]
        // Initialize Firebase Database
        database = Firebase.firestore
        // [END initialize_database]

        setContent {
            PadelHubTheme {
                AppNavigation()
            }
        }
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        //Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun createAccount(email: String, password: String, navController: NavController, context: Context) {
        // [START create_user_with_email]
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(
                        context,
                        "Te has registrado con exito.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    navController.navigate(route = AppScreens.HomeScreen_Inicio.route)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
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


    private fun signIn(email: String, password: String, navController: NavController, context: Context) {
        // [START sign_in_with_email]
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
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
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
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

    fun sendEmailVerification() {
        // [START send_email_verification]
        /*
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }

         */
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun reload() {
    }

    companion object {
        private const val TAG = "EmailPassword"
    }

    @Composable
    fun LoginScreen(navController: NavController){
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current
        Surface(color = Color.White) {
            Column(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_padelhub2),
                    contentDescription = "Logo",
                    modifier = androidx.compose.ui.Modifier.size(300.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Introduce tu correo electrónico") },
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

                Column(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                           signIn(email,password,navController, context = context)
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth()

                    ) {
                        Text("Iniciar sesión")
                    }
                    Button(
                        onClick = {
                            navController.navigate(route = AppScreens.HomeScreen_Registro.route)
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                    ) {
                        Text("¿Todavía no eres usuario? Regístrate")
                    }
                }
            }
        }
    }

    @Composable
    fun RegisterScreen(navController: NavController){

        var nombre by remember { mutableStateOf("") }
        var edad by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var password2 by remember { mutableStateOf("") }
        val context = LocalContext.current
        //demáss datos que queramos guardar del usuario

        Surface(color = Color.White) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_padelhub2),
                    contentDescription = "Logo",
                    modifier = Modifier.size(200.dp)
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text("Edad") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Introduce tu correo electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                OutlinedTextField(
                    value = password2,
                    onValueChange = { password2 = it },
                    label = { Text("Repita su contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        //Aquí habría que llamar a los métodos de registro en la base de datos
                        //pasandole los parámetros recogidos en los textfield
                        //Dependiendo de si se ha podido registrar al usuario o no navegaremos a una pantalla u otra
                        //createAccount(email,password)
                        onClick = {
                            createAccount(email, password, navController, context)
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                    ) {
                        Text("Registrarme")
                    }
                    Button(
                        onClick = {
                            navController.navigate(route = AppScreens.HomeScreen_Login.route)
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                    ) {
                        Text("Volver a inicio de sesión")
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PadelHubTheme {
        Greeting("Android")
    }
}
