package eu.golovkov.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PColor.Purple40,
    secondary = PColor.PurpleGrey40,
    tertiary = PColor.Pink40,
)

private val DarkColors = darkColorScheme(
    primary = PColor.Purple80,
    secondary = PColor.PurpleGrey80,
    tertiary = PColor.Pink80,
)

@Composable
fun PTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}