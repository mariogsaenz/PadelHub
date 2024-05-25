package com.example.padelhub.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.example.padelhub.MainActivity
import com.example.padelhub.R
import com.example.padelhub.modelo.ChatMessage
import com.example.padelhub.modelo.Chatroom
import com.example.padelhub.modelo.Partido
import com.example.padelhub.modelo.Usuario
import com.example.padelhub.persistencia.GestionChat
import com.example.padelhub.persistencia.GestionPartido
import com.example.padelhub.persistencia.GestionUsuario
import com.example.padelhub.ui.navigation.AppScreens
import com.example.padelhub.ui.utils.CustomOutlinedTextField
import com.example.padelhub.ui.utils.CustomOutlinedTextField2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.hbb20.CountryCodePicker
import kotlinx.coroutines.runBlocking
import java.util.Date


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenChat(navController: NavController, database: FirebaseFirestore, auth: FirebaseAuth) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) {
        ContenidoChatRooms(navController, database, auth)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenChatContent(navController: NavController, chatroomId: String, database: FirebaseFirestore, auth: FirebaseAuth) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) {
        ContenidoAppChat(chatroomId,database,auth)
    }
}


data class Message(val type: MessageType, val content: Any) {
    enum class MessageType {
        TEXT
    }
}
@Composable
fun MessageItem(message: ChatMessage, UsuarioActualId: String) {
    if(message.senderId==UsuarioActualId) {
        TextMessageMe(message.message)
    }else{
        TextMessageOther(message.message)
    }
}

@Composable
fun TextMessageMe(text: String, color: Color = Color.White) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = text,
            color = color,
            modifier = Modifier
                .background(Color(0xFF1E88E5), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.person_24px),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun TextMessageOther(text: String, color: Color = Color.White) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.person_24px),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = color,
            modifier = Modifier
                .background(Color(0xFF757575), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContenidoAppChat(chatroomId: String, database: FirebaseFirestore,auth: FirebaseAuth) {
    var messageText by remember { mutableStateOf(TextFieldValue()) }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val context = LocalContext.current
    var usuarioActivo: Usuario? = null
    runBlocking {
        runBlocking {
            usuarioActivo = GestionUsuario().getUsuarioActual(auth,database)
        }
    }

    val docRef = database.collection("chatroom").document(chatroomId)
    docRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w("Chat", "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
            val chatroom = snapshot.toObject(Chatroom::class.java)
            if (chatroom != null) {
                messages = chatroom.messages
            }
            Log.d("Chat", "Current data: ${snapshot.data}")
        } else {
            Log.d("Chat", "Current data: null")
        }
    }

    val backgroundImage: Painter = painterResource(id = R.drawable.fondo)
    Box(modifier = Modifier
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
                    text = "Chat",
                    style = MaterialTheme.typography.titleLarge,
                    color=Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    items(messages) { message ->
                        usuarioActivo?.let { MessageItem(message = message, it.id) }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp, 8.dp, 80.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = {
                            messageText = it
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (messageText.text.isNotEmpty()) {
                                runBlocking {
                                    GestionChat().enviarMensaje(chatroomId,messageText.text,database, auth)
                                }
                                messageText = TextFieldValue()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005D72))
                    ){
                        Icon(Icons.Filled.Send, contentDescription = "Enviar")
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoChatRooms(navController: NavController, database: FirebaseFirestore, auth: FirebaseAuth) {
    val backgroundImage: Painter = painterResource(id = R.drawable.fondo)
    var filtroBusqueda by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
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
                    text = "Chats",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 56.dp, topEnd = 56.dp))
                )
                {
                    ListarChats(navController, database, auth)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListarChats(navController: NavController, database: FirebaseFirestore, auth: FirebaseAuth) {

    LazyColumn(
        flingBehavior = ScrollableDefaults.flingBehavior(),
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var myList = mutableListOf<Chatroom>()
        runBlocking {
                myList = GestionChat().fetch(database,auth).toMutableList()
            Log.d("NOMBRESSSSS: ", myList.toString())
        }


        items(myList) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                ) {
                    CardChat(chatroom = it, database, auth, navController)
                }
            }
        }
        item { Spacer(modifier = Modifier.padding(35.dp)) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardChat(chatroom: Chatroom, database: FirebaseFirestore, auth: FirebaseAuth, navController: NavController) {

    Card(
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
        onClick = {
            navController.navigate(route = AppScreens.HomeScreen_Chat_Content.route+"/${chatroom.chatroomId}")
        }
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
                    text = chatroom.userIds.size.toString() + "/4",
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
                    text = chatroom.chatroomId,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00272B),
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
            }
        }

    }
}

