package com.example.padelhub.ui.navigation

sealed class AppScreens(val route: String) {
    object HomeScreen_Inicio: AppScreens("pantalla_inicio")
    object HomeScreen_Pistas: AppScreens("pantalla_pistas")
    object HomeScreen_Chat: AppScreens("pantalla_chat")
    object HomeScreen_Perfil: AppScreens("pantalla_perfil")
    object HomeScreen_Inicio_CrearPartido: AppScreens("crear_partido")
    object HomeScreen_Pistas_AnadirPista: AppScreens("anadir_pista")
    object HomeScreen_Inicio_BuscarPartidos: AppScreens("buscar_partidos")
}
