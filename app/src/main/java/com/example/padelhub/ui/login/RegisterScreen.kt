package com.example.padelhub.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.padelhub.R
import com.example.padelhub.ui.navigation.AppScreens

@Composable
fun RegisterScreen(navController: NavController){

    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
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
                        navController.navigate(route = AppScreens.HomeScreen_Inicio.route)
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