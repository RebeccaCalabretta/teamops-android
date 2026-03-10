package io.github.rebeccacalabretta.teamops.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Blue90 = Color(0xFFE9E8FF)
val Blue80 = Color(0xFFC6C3FF)
val Blue60 = Color(0xFF6E67F2)
val Blue40 = Color(0xFF0E07E0) // primary
val Blue20 = Color(0xFF09059A)
val Blue10 = Color(0xFF040252)

// Button gradients
val PrimaryButtonGradient = Brush.verticalGradient(
    colors = listOf(Blue40, Blue20)
)

val DisabledButtonGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFB0BEC5),
        Color(0xFF90A4AE)
    )
)

// Neutral surfaces
val SurfaceLight = Color(0xFFF7F7FB)
val SurfaceVariantLight = Color(0xFFECECFA)
val OnSurfaceLight = Color(0xFF1C1C1C)

val SurfaceDark = Color(0xFF0B0B14)
val SurfaceVariantDark = Color(0xFF1A1A2E)
val OnSurfaceDark = Color(0xFFE6E6E6)

// Backgrounds
val BackgroundLight = Color(0xFFF7F7FB)
val BackgroundDark = Color(0xFF0B0B14)

val OnBackgroundLight = Color(0xFF1C1C1C)
val OnBackgroundDark = Color(0xFFE6E6E6)

// Outline (dividers / borders)
val OutlineLight = Color(0xFFCAC8FF)
val OutlineDark = Color(0xFF3B3A55)

val Error = Color(0xFFD32F2F)
val Success = Color(0xFF46AF4B)