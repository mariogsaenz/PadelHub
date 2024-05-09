package com.example.padelhub.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.padelhub.R

import com.example.padelhub.ui.screens.BottomNavigation
import com.example.padelhub.ui.theme.PadelHubTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenChat(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) {
        ContenidoAppChat()
    }
}

data class Message(val type: MessageType, val content: Any) {
    enum class MessageType {
        TEXT
    }
}
@Composable
fun MessageItem(message: Message) {
    when (message.type) {
        Message.MessageType.TEXT -> {
            TextMessage(message.content as String)
        }
    }
}

@Composable
fun TextMessage(text: String, color: Color = Color.White) {
    Row(
        modifier = Modifier.padding(8.dp)
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
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun ContenidoAppChat() {
    var messageText by remember { mutableStateOf(TextFieldValue()) }
    var messages by remember { mutableStateOf(listOf<Message>()) }

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
                        MessageItem(message = message)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp,0.dp,8.dp,80.dp),
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
                                messages = messages + Message(Message.MessageType.TEXT, messageText.text)
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

