package com.example.padelhub

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.padelhub.ui.navigation.AppNavigation
import com.example.padelhub.ui.theme.PadelHubTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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
        // [END initialize_database

        setContent {
            PadelHubTheme {
                AppNavigation(auth, database)
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

    private fun reload() {
    }

    companion object {
        const val TAG = "EmailPassword"
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

