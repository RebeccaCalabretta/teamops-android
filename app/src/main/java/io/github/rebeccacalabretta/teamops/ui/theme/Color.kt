package io.github.rebeccacalabretta.teamops.ui.theme

import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color

//Cyan colors
val Cyan20 = Color(0xFF00525B)
val Cyan40 = Color(0xFF0E8FA0)
val Cyan60 = Color(0xFF13AFC1)
val Cyan80 = Color(0xFF7AD6E1)
val Cyan90 = Color(0xFFBEECEF)

//brush colors
val CyanButtonGradient = verticalGradient(
    colors = listOf(Cyan80, Cyan20)
)

val DisabledButtonBrush = verticalGradient(
    colors = listOf(
        Color(0xFFB0BEC5),
        Color(0xFF90A4AE)
    )
)

// Neutral colors
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF1C1C1C)

val SurfaceDark = Color(0xFF0F1B1D)
val OnSurfaceDark = Color(0xFFE6E6E6)