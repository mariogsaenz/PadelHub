package com.example.padelhub.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.padelhub.R
import com.example.padelhub.persistencia.GestionUsuario
import com.example.padelhub.ui.navigation.AppScreens
import com.example.padelhub.ui.theme.verdePadel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenPerfil(navController: NavController, auth: FirebaseAuth) {
    Scaffold(
        topBar = { TopBarApp() },
        bottomBar = { BottomNavigation(navController) }
    ) {
        ContenidoAppPerfil(navController, auth)
    }
}

@Composable
fun ContenidoAppPerfil(navController: NavController, auth: FirebaseAuth) {
    val backgroundImage: Painter = painterResource(id = R.drawable.fondo)
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
                .blur(radiusX = 8.dp, radiusY = 8.dp),
            colorFilter = ColorFilter.tint(Color.Gray.copy(alpha = 0.7f), BlendMode.DstIn),
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Contenido de la pantalla de perfil",
                color = Color.White
            )
            Button(
                onClick = {
                    GestionUsuario().signOut(auth, navController, context)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7ED957)),
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)

            ) {
                Text(
                    "Cerrar sesión",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

