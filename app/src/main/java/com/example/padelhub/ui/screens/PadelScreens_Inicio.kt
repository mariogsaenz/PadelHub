package com.example.padelhub.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.example.padelhub.ui.theme.verdePadel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.padelhub.R
import com.example.padelhub.modelo.Partido
import com.example.padelhub.persistencia.GestionPartido
import com.example.padelhub.ui.navigation.AppScreens
import com.example.padelhub.ui.utils.CustomOutlinedTextField
import com.example.padelhub.ui.utils.DatePicker
import com.example.padelhub.ui.utils.TimePicker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenInicio(navController: NavController, database: FirebaseFirestore) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) {
        ContenidoAppInicio(navController,database)
    }
}

@Composable
fun ContenidoAppInicio(navController: NavController, database: FirebaseFirestore) {
    val backgroundImage: Painter = painterResource(id = R.drawable.fondo)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 20.dp),
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
                    text = "Partidos",
                    style = MaterialTheme.typography.titleLarge,
                    color=Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                Button(
                    onClick = { navController.navigate("crear_partido")},
                    modifier = Modifier
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF599B3D))
                )
                {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.add_24px), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Crear partido"
                    )
                }

            }
            BuscarPartidosScreen(navController,database)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrearPartidoScreen(navController: NavController, database: FirebaseFirestore) {
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomOutlinedTextField(value = nombre, onValueChange = {nombre = it}, label = "Nombre del partido")

            DatePicker(
                label = "Fecha del partido",
                value = fecha,
                onValueChange = { fecha = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            TimePicker(
                label = "Hora del partido",
                value = hora,
                onValueChange = { hora = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            CustomOutlinedTextField(value = ubicacion, onValueChange = {ubicacion = it}, imeAction = ImeAction.Done, label = "Ubicación del partido")

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
                        val partido = hashMapOf(
                            "nombre" to nombre,
                            "fecha" to fecha,
                            "hora" to hora,
                            "ubicacion" to ubicacion,
                            "estado" to true
                        )
                        database.collection("partido").add(partido)
                            .addOnSuccessListener { Log.d("Partido", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("Partido", "Error writing document", e) }
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7ED957)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear partido")
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
fun BuscarPartidosScreen(navController: NavController, database: FirebaseFirestore) {
    LazyColumn(
        flingBehavior = ScrollableDefaults.flingBehavior(),
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            /*Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = "PARTIDOS DISPONIBLES",
                style = MaterialTheme.typography.titleLarge,
                color=Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )*/
        }
        var myList = mutableListOf<Partido>()
        runBlocking {
            myList = GestionPartido().fetchPartidos(database).toMutableList()
        }

        items(myList) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 0.dp,
                        color = Color.LightGray,
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    ExpandableCard(partido = it, database)
                }
            }
        }
    }
}


@Composable
fun ExpandableCard(partido: Partido, database: FirebaseFirestore) {

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
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
                    text = "Creado por:",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                )
                Text(
                    text = "DavidPlayer",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF599B3D),
                )

            }

            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = partido.nombre,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF599B3D),
                )
                Text(
                    text = partido.ubicacion,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 20.sp,
                    color = Color(0xFF599B3D),
                )
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Crear partido"
                    )
                    Text(
                        text = partido.fecha,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.baseline_access_alarms_24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Crear partido"
                    )
                    Text(
                        text = partido.hora,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp
                    )

                }
                Button(
                    onClick = {database.collection("partido").get()},
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0097B2))
                ){
                    Text(
                        "Solicitar unirme",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )
                }
            }
        }
    }
}

