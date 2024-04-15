package com.example.padelhub.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.padelhub.ui.screens.AnadirPistaScreen
import com.example.padelhub.ui.screens.BuscarPartidosScreen
import com.example.padelhub.ui.screens.CrearPartidoScreen
import com.example.padelhub.ui.screens.HomeScreenChat
import com.example.padelhub.ui.screens.HomeScreenInicio
import com.example.padelhub.ui.screens.HomeScreenPerfil
import com.example.padelhub.ui.screens.HomeScreenPistas
import com.example.padelhub.ui.screens.LoginScreen
import com.example.padelhub.ui.screens.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(auth: FirebaseAuth, database: FirebaseFirestore){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen_Login.route){
        composable(route=AppScreens.HomeScreen_Login.route){
            LoginScreen(navController, auth)
        }
        composable(route=AppScreens.HomeScreen_Registro.route){
             RegisterScreen(navController, auth, database)
        }
        composable(route=AppScreens.HomeScreen_Inicio.route){
            HomeScreenInicio(navController)
        }
        composable(route=AppScreens.HomeScreen_Pistas.route){
            HomeScreenPistas(navController)
        }
        composable(route=AppScreens.HomeScreen_Chat.route){
            HomeScreenChat(navController)
        }
        composable(route=AppScreens.HomeScreen_Perfil.route){
            HomeScreenPerfil(navController)
        }
        composable(route=AppScreens.HomeScreen_Inicio_CrearPartido.route){
            CrearPartidoScreen(navController, database)
        }
        composable(route=AppScreens.HomeScreen_Pistas_AnadirPista.route){
            AnadirPistaScreen(navController)
        }
        composable(route=AppScreens.HomeScreen_Inicio_BuscarPartidos.route){
            BuscarPartidosScreen(navController)
        }

    }
}
