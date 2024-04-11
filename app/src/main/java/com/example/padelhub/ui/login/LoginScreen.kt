package com.example.padelhub.ui.login

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.padelhub.R
import com.example.padelhub.ui.navigation.AppScreens
import com.example.padelhub.ui.screens.DatePicker
import com.example.padelhub.ui.screens.TimePicker
import java.lang.reflect.Modifier
import java.time.format.TextStyle

@Composable
fun LoginScreen(navController: NavController){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    //Aquí habría que llamar a los métodos de inicio de sesión en la base de datos
                    //pasandole los parámetros email y password recogidos en los textfield
                    //Dependiendo de si se ha podido iniciar sesión o no navegaremos a una pantalla u otra
                    //signIn(email,password)
                    onClick = {
                        navController.navigate(route = AppScreens.HomeScreen_Inicio.route)
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