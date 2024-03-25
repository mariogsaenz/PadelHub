package com.example.padelhub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.padelhub.ui.screens.HomeScreenChat
import com.example.padelhub.ui.screens.HomeScreenInicio
import com.example.padelhub.ui.screens.HomeScreenPerfil
import com.example.padelhub.ui.screens.HomeScreenPistas

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen_Inicio.route){
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
    }
}