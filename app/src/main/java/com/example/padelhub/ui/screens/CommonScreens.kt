package com.example.padelhub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.padelhub.R
import com.example.padelhub.ui.navigation.AppScreens


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarApp(){

    /*TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_padelhub3),
                    contentDescription = "Logo",
                    modifier = Modifier.size(400.dp)
                        .padding(0.dp,50.dp)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )*/

    TopAppBar(
        title = { Text(
            text="PADEL HUB",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
        ) },
        modifier = Modifier.fillMaxWidth()
            .background(Color.Blue)
    )
}

@Composable
fun BottomNavigation(navController: NavController) {
    Surface(
        color = Color.Transparent
    ) {
        BottomAppBar(
            containerColor = Color.Transparent, //Color del container
            contentColor = Color.White, //Color de los iconos
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(35))
                        .background(Color.Black),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Inicio.route) }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.home_24px), // Reemplaza R.drawable.ic_home con el recurso de tu icono de inicio
                            contentDescription = "Inicio"
                        )
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Pistas.route) }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.sports_baseball_24px__1_), // Reemplaza R.drawable.ic_court con el recurso de tu icono de pistas
                            contentDescription = "Pistas"
                        )
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Chat.route) }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.chat_24px), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                            contentDescription = "Chat"
                        )
                    }
                    IconButton(onClick = { navController.navigate(route = AppScreens.HomeScreen_Perfil.route) }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.person_24px), // Reemplaza R.drawable.ic_profile con el recurso de tu icono de perfil
                            contentDescription = "Perfil"
                        )
                    }
                }
            }
        )
    }
}