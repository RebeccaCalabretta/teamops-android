package io.github.rebeccacalabretta.teamops.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(

    primary = Blue40,
    onPrimary = Color.White,

    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,

    secondary = Blue60,
    onSecondary = Color.White,

    secondaryContainer = Blue80,
    onSecondaryContainer = Blue10,

    tertiary = Blue80,
    onTertiary = Color.Black,

    background = BackgroundLight,
    onBackground = OnBackgroundLight,

    surface = SurfaceLight,
    onSurface = OnSurfaceLight,

    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Blue40,

    outline = OutlineLight,

    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.15f),
    onErrorContainer = Error
)

private val DarkColorScheme = darkColorScheme(

    primary = Blue80,
    onPrimary = Blue10,

    primaryContainer = Blue40,
    onPrimaryContainer = Color.White,

    secondary = Blue80,
    onSecondary = Blue10,

    secondaryContainer = Blue40,
    onSecondaryContainer = Color.White,

    tertiary = Blue60,
    onTertiary = Color.White,

    background = BackgroundDark,
    onBackground = OnBackgroundDark,

    surface = SurfaceDark,
    onSurface = OnSurfaceDark,

    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Blue80,

    outline = OutlineDark,

    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.25f),
    onErrorContainer = Color.White
)

/* Other default colors to override
background = Color(0xFFFFFBFE),
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/


@Composable
fun TeamOpsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}