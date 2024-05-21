package com.example.padelhub.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.padelhub.R
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Usuario
import com.example.padelhub.persistencia.GestionPartido
import com.example.padelhub.persistencia.GestionUsuario
import com.example.padelhub.ui.navigation.AppScreens
import com.example.padelhub.ui.theme.verdePadel
import com.example.padelhub.ui.utils.CustomOutlinedTextField
import com.example.padelhub.ui.utils.DatePicker
import com.example.padelhub.ui.utils.TimePicker
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
                contentScale = ContentScale.FillBounds
            )
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        onClick = { navController.navigate("modificar_datos")},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp)
                    )
                    {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(id = R.drawable.ajustes_logo),
                            contentDescription = "Modificar mis datos"
                        )
                    }
                    Button(
                        onClick = { GestionUsuario().signOut(auth, navController, context)},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp)
                    )
                    {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(id = R.drawable.logout_logo),
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            }
            InfoUsuarioScreen(navController,auth,database)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoUsuarioScreen(navController: NavController, auth: FirebaseAuth, database: FirebaseFirestore) {
    val context = LocalContext.current
    var usuarioActivo: Usuario? = null
    runBlocking {
        usuarioActivo = GestionUsuario().getUsuarioActual(auth,database)
    }
    Card(
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
        modifier = Modifier
            .fillMaxHeight()
            .padding(5.dp, 5.dp, 5.dp, 100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp) //aqui esta el error de la linea blanca que sale
                .fillMaxWidth()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                usuarioActivo?.let {
                    Image(
                        painter = painterResource(id = R.drawable.person_24px),
                        contentDescription = "imagenUsuario",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .padding(vertical = 10.dp),
                        contentScale = ContentScale.Fit
                    )
                    Column(

                    ){
                        Text(
                            text = it.nombre,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00272B),
                        )
                        val edadString = it.edad.toString()
                        Text(
                            text = buildAnnotatedString{
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00272B),
                                    )
                                ){
                                    append("Edad: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF00272B)
                                    )
                                ){
                                    append(edadString)
                                }
                            },
                        )
                        Text(
                            text = buildAnnotatedString{
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00272B)
                                    )
                                ){
                                    append("Email: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF00272B)
                                    )
                                ){
                                    append(it.email)
                                }
                            },
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 7.dp))
            //AQUI AÑADIMOS LA LISTA DE PARTIDOS CREADOS POR EL USUARIO
            //¿Se podría añadir también una lista con los partidos en los que está inscrito?
            //Así solo se muestran los partidos que ha creado el.
            //Sería interesante añadir dos botones o una forma de filtrar esto para que te puedan salir partidos creados por ti y/o partidos en los que estas inscrito
            Column(modifier = Modifier
                .clip(RoundedCornerShape(topStart = 56.dp, topEnd = 56.dp))
            ){
                LazyColumn(
                    flingBehavior = ScrollableDefaults.flingBehavior(),
                    state = rememberLazyListState(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var myList = mutableListOf<Partido>()
                    runBlocking {
                        myList = GestionUsuario().getPartidosPropietario(auth,database).toMutableList()
                    }
                    Log.d("Lista de partidos del usuario actual: ", myList.toString())
                    items(myList) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Column(
                            ) {
                                ExpandableCardPerfil(partido = it, database)
                                //Se usara ExpandableCardPerfil2 para los partidos de los que no somos propietarios y queremos unirnos
                            }
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ModificarDatosScreen(auth: FirebaseAuth, navController: NavController, database: FirebaseFirestore) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomOutlinedTextField(value = nombre, onValueChange = {nombre = it}, label = "Nuevo nombre de usuario")
            CustomOutlinedTextField(value = edad, onValueChange = {edad = it}, imeAction = ImeAction.Done, label = "Nueva edad")

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
                        val usuario = hashMapOf(
                            "nombre" to nombre,
                            "edad" to edad,
                        )
                        var edadInt = edad.toInt()
                        runBlocking {
                            GestionUsuario().changeDatosUsuario(auth,database,nombre,edadInt)
                        }
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005D72)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Modificar datos")
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

@Composable
fun ExpandableCardPerfil(partido: Partido, database: FirebaseFirestore) {

    Card(
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    text = partido.jugadores.size.toString() + "/4",
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
                    text = partido.nombre,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00272B),
                )
                Text(
                    text = partido.ubicacion,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 20.sp,
                    color = Color(0xFF00272B),
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Fecha partido"
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp),
                        text = partido.fecha,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.baseline_access_alarms_24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Hora partido"
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp),
                        text = partido.hora,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp
                    )

                }
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Button(
                    onClick = {
                        runBlocking {
                            GestionPartido().changeEstadoToAcabado(partido, database)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCB3234))
                ){
                    Text(
                        "Cancelar partido",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableCardPerfil2(partido: Partido, database: FirebaseFirestore) {

    Card(
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    text = partido.jugadores.size.toString() + "/4",
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
                    text = partido.nombre,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00272B),
                )
                Text(
                    text = partido.ubicacion,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 20.sp,
                    color = Color(0xFF00272B),
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Fecha partido"
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp),
                        text = partido.fecha,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.baseline_access_alarms_24), // Reemplaza R.drawable.ic_chat con el recurso de tu icono de chat
                        contentDescription = "Hora partido"
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp),
                        text = partido.hora,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 13.sp
                    )

                }
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Button(
                    onClick = {/*AQUI HABRÍA QUE IMPLEMENTAR LA ELIMINACIÓN DEL PARTIDO EN LA BASE DE DATOS, se deberá notificar al resto de jugadores apuntados al partido*/},
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCB3234))
                ){
                    Text(
                        "Abandonar el partido",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )
                }
            }
        }
    }
}

