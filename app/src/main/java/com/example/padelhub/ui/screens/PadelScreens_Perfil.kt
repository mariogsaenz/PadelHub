package com.example.padelhub.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

/**
 * Composable principal que sirve como contenedor
 * que despliega el contenido dependiendo del estado de la interfaz
 * y el tamaño de la ventana
 */

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.padelhub.R
import com.example.padelhub.persistencia.GestionUsuario
import com.example.padelhub.ui.navigation.AppScreens
import com.example.padelhub.ui.theme.verdePadel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenPerfil(navController: NavController, auth: FirebaseAuth, database: FirebaseFirestore) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) {
        ContenidoAppPerfil(navController, auth, database)
    }
}

@Composable
fun ContenidoAppPerfil(navController: NavController, auth: FirebaseAuth, database: FirebaseFirestore) {
    val backgroundImage: Painter = painterResource(id = R.drawable.fondo)
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
            // Replace with your image id
            painterResource(id = R.drawable.fondo),
            contentScale = ContentScale.FillBounds)
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize(),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Perfil",
                    style = MaterialTheme.typography.titleLarge,
                    color=Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            InfoUsuarioScreen(navController,auth,database)
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoUsuarioScreen(navController: NavController, auth: FirebaseAuth, database: FirebaseFirestore) {
    val context = LocalContext.current
    runBlocking {
        //var usuarioActivo = GestionUsuario.getUsuarioActivo(database)
    }
    Card(
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
        modifier = Modifier
            .fillMaxHeight()
            .padding(5.dp,5.dp,5.dp,100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.person_24px),
                    contentDescription = "imagenUsuario",
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .padding(vertical = 10.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "Mario Gil Sáenz",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00272B),
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 7.dp))
            Button(
                onClick = {
                    //Llamada a la persistencia para modificar los datos del usuario
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(width = 2.dp,
                        color = Color(0xFF005D72),
                        shape = RoundedCornerShape(40.dp))
            ) {
                Text(
                    "Modificar mis datos",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF005D72)
                )
            }
            Button(
                onClick = {
                    GestionUsuario().signOut(auth, navController, context)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005D72)),
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "Cerrar sesión",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

