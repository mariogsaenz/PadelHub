package com.example.padelhub.ui.screens

import com.example.padelhub.ui.navigation.AppScreens
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

/**
 * Composable principal que sirve como contenedor
 * que despliega el contenido dependiendo del estado de la interfaz
 * y el tamaño de la ventana
 */

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.padelhub.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenPistas(navController: NavController) {
    Scaffold(
        topBar = { TopBarAppPistas() },
        bottomBar = { BottomNavigationPistas(navController) }
    ) {
        ContenidoAppPistas()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarAppPistas(){
    TopAppBar(
        title = { Text(text="PADEL HUB") },
        modifier = Modifier.fillMaxWidth()
            .background(Color.Blue)
    )
}

@Composable
fun ContenidoAppPistas() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Contenido de la pantalla de pistas")
        //AQUÍ HABRÁ QUE PONER EL CONTENIDO QUE QUERAMOS MOSTRAR
    }
}

@Composable
fun BottomNavigationPistas(navController: NavController) {
    Surface(color = Color.White) {
        BottomAppBar(
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Inicio.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.home_fill0_wght400_grad0_opsz24), // Reemplaza R.drawable.ic_home con el recurso de tu icono de inicio
                            contentDescription = "Inicio"
                        )
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Pistas.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sports_baseball_fill0_wght400_grad0_opsz24), // Reemplaza R.drawable.ic_court con el recurso de tu icono de pistas
                            contentDescription = "Pistas"
                        )
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Chat.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chat_fill0_wght400_grad0_opsz24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                            contentDescription = "Chat"
                        )
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Perfil.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.person_fill0_wght400_grad0_opsz24), // Reemplaza R.drawable.ic_profile con el recurso de tu icono de perfil
                            contentDescription = "Perfil"
                        )
                    }
                }
            }
        )
    }
}




