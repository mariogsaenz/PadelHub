package com.example.padelhub.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.padelhub.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction = ImeAction.Next,
    typePassword: Boolean = false
) {
    var showPassword by remember { mutableStateOf(value = false) }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color=Color.LightGray,
                fontWeight = FontWeight.Light
            )
        },
        textStyle = TextStyle(color = Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White
        ),
        shape = RoundedCornerShape(percent = 20),
        singleLine = true,
        keyboardOptions = if (!typePassword) {
            KeyboardOptions(
                imeAction = imeAction
            )
        } else {
            KeyboardOptions.Default.copy(
                autoCorrect = true,
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            )
        },
        keyboardActions = if(imeAction== ImeAction.Done){
            KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        }else KeyboardActions(),
        visualTransformation = if(typePassword){
            if (showPassword) {

                VisualTransformation.None

            } else {

                PasswordVisualTransformation()

            }
        } else VisualTransformation.None,
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.icono_logo_fino),
                contentDescription = "Imagen del icono"
            )
        },
        trailingIcon = typePassword.takeIf { it }?.let {
            {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        }
    )
}

