package com.example.padelhub.ui.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.padelhub.modelo.Chatroom
import com.example.padelhub.modelo.Partido
import com.example.padelhub.ui.screens.AnadirPistaScreen
import com.example.padelhub.ui.screens.BuscarPartidosScreen
import com.example.padelhub.ui.screens.CrearPartidoScreen
import com.example.padelhub.ui.screens.HomeScreenChat
import com.example.padelhub.ui.screens.HomeScreenInicio
import com.example.padelhub.ui.screens.HomeScreenPerfil
import com.example.padelhub.ui.screens.HomeScreenPistas
import com.example.padelhub.ui.screens.LoginScreen
import com.example.padelhub.ui.screens.ModificarDatosScreen
import com.example.padelhub.ui.screens.RegisterScreen
import com.example.padelhub.ui.screens.ScreenChatContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(auth: FirebaseAuth, database: FirebaseFirestore, activity: Activity){
    val navController = rememberNavController()
    var filtroBusqueda by remember { mutableStateOf("") }
    var codigoOTP by remember { mutableStateOf("") }
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen_Login.route){
        composable(route=AppScreens.HomeScreen_Login.route){
            LoginScreen(navController, auth)
        }
        composable(route=AppScreens.HomeScreen_Registro.route){
             RegisterScreen(navController, auth, database)
        }
        composable(route=AppScreens.HomeScreen_Inicio.route){
            HomeScreenInicio(navController,database, auth)
        }
        composable(route=AppScreens.HomeScreen_Pistas.route){
            HomeScreenPistas(navController,database)
        }
        composable(route=AppScreens.HomeScreen_Chat_List.route){
            HomeScreenChat(navController, database, auth)
        }
        composable(route=AppScreens.HomeScreen_Chat_Content.route+"/{chatroomId}",
            arguments = listOf(navArgument("chatroomId") { type = NavType.StringType })
        ){
                backStackEntry ->
            val id = backStackEntry.arguments?.getString("chatroomId")
            requireNotNull(id)
            ScreenChatContent(navController,id, database, auth)
        }
        composable(route=AppScreens.HomeScreen_Perfil.route){
            HomeScreenPerfil(navController, auth, database)
        }
        composable(route=AppScreens.HomeScreen_Inicio_CrearPartido.route){
            CrearPartidoScreen(navController, database, auth)
        }
        composable(route=AppScreens.HomeScreen_Pistas_AnadirPista.route){
            AnadirPistaScreen(navController, database)
        }
        composable(route=AppScreens.HomeScreen_Inicio_BuscarPartidos.route){
            BuscarPartidosScreen(filtroBusqueda,navController, database, auth)
        }
        composable(route=AppScreens.HomeScreen_Perfil_ModificarDatos.route){
            ModificarDatosScreen(auth, navController, database)
        }
    }
}

