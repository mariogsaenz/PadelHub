package com.example.padelhub.ui.screens

import com.example.padelhub.ui.navigation.AppScreens
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.padelhub.R
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Pista
import com.example.padelhub.persistencia.GestionPartido
import com.example.padelhub.persistencia.GestionPista
import com.example.padelhub.ui.theme.verdePadel
import com.example.padelhub.ui.utils.CustomOutlinedTextField
import com.example.padelhub.ui.utils.CustomOutlinedTextField2
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenPistas(navController: NavController, database: FirebaseFirestore) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) {
        ContenidoAppPistas(navController,database)
    }
}

@Composable
fun ContenidoAppPistas(navController: NavController, database: FirebaseFirestore) {
    val backgroundImage: Painter = painterResource(id = R.drawable.fondo)
    var filtroBusqueda by remember { mutableStateOf("") }
    Box(modifier = Modifier
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
                    text = "Pistas",
                    style = MaterialTheme.typography.titleLarge,
                    color=Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                Button(
                    onClick = { navController.navigate("crear_partido")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                )
                {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = R.drawable.baseline_add_circle_outline_24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Añadir pista"
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomOutlinedTextField2(
                    value = filtroBusqueda,
                    onValueChange = {filtroBusqueda = it},
                    imeAction = ImeAction.Done,
                    label = "Introduce el nombre de la pista"
                )
            }
            Column(modifier = Modifier
                .clip(RoundedCornerShape(topStart = 56.dp, topEnd = 56.dp)))
            {
                BuscarPistasScreen(filtroBusqueda,navController,database)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnadirPistaScreen(navController: NavController, database: FirebaseFirestore) {
    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var numeroPistas by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomOutlinedTextField(value = nombre, onValueChange = {nombre = it}, imeAction = ImeAction.Done, label = "Nombre de las instalaciones")
            CustomOutlinedTextField(value = ubicacion, onValueChange = {ubicacion = it}, imeAction = ImeAction.Done, label = "Ubicación")
            CustomOutlinedTextField(value = numeroPistas, onValueChange = {numeroPistas = it}, imeAction = ImeAction.Done, label = "Número de pistas")
            CustomOutlinedTextField(value = precio, onValueChange = {precio = it}, imeAction = ImeAction.Done, label = "Precio")

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        val pista = hashMapOf(
                            "nombre" to nombre,
                            "ubicacion" to ubicacion,
                            "numeroPistas" to numeroPistas,
                            "precio" to precio,
                        )
                        database.collection("pista").add(pista)
                            .addOnSuccessListener { Log.d("Pista", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("Pista", "Error writing document", e) }
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00272B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir pistas")
                }
                ClickableText(
                    text = AnnotatedString("Cancelar") ,
                    onClick = { navController.navigateUp() },
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(0.dp, 10.dp)
                        .drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val verticalOffset = size.height - 2.sp.toPx()
                            drawLine(
                                color = Color.Black,
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        },
                )
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BuscarPistasScreen(filtroBusqueda: String, navController: NavController, database: FirebaseFirestore) {
    LazyColumn(
        flingBehavior = ScrollableDefaults.flingBehavior(),
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var myList = mutableListOf<Pista>()
        var myList2 = mutableListOf<Pista>()
        runBlocking {
            if(filtroBusqueda==""){
                myList = GestionPista().fetchPistas(database).toMutableList()
            }
            else{
                myList2 = GestionPista().fetchPistasNombre(filtroBusqueda,database).toMutableList()
            }
        }

        items(myList) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                ) {
                    ExpandableCard(pista = it, database)
                }
            }
        }
        items(myList2) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                ) {
                    ExpandableCard(pista = it, database)
                }
            }
        }
        item { Spacer(modifier = Modifier.padding(35.dp)) }
    }
}

@Composable
fun ExpandableCard(pista: Pista, database: FirebaseFirestore) {

    var expanded by remember { mutableStateOf (false) }

    Card(
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.person_24px),
                    contentDescription = "imagenPartido",
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .padding(vertical = 10.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = pista.precio + "€",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = pista.nombre,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00272B),
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ubicacion_logo),
                        contentDescription = "Ubicación pista"
                    )
                    Text(
                        text = pista.ubicacion,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Text(
                    text = pista.numeroPistas + " pistas disponibles",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Button(
                    onClick = {database.collection("pista").get()},
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005D72))
                ){
                    Text("Contactar con la pista")
                }
            }
        }
    }
}
