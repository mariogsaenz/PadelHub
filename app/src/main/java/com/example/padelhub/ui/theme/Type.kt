package com.example.padelhub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.padelhub.R

val nanit_regular = FontFamily(
    Font(R.font.nanit_regular)
)
val nanit_light = FontFamily(
    Font(R.font.nanit_light)
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = nanit_light,
        fontSize = 15.sp
    ),
    labelMedium = TextStyle(
        fontFamily = nanit_regular,
        fontSize = 18.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = nanit_regular,
        fontSize = 36.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
)
