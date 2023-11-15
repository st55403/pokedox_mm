package eu.golovkov.core.ui

import androidx.compose.ui.graphics.Color
import eu.golovkov.core.ui.theme.PColor

object CardBackgroundColor {
    fun getColor(name: String): Pair<Color, Color> =
        when (name) {
            "grass" -> Pair(PColor.GrassLight, PColor.GrassDark)
            "fire" -> Pair(PColor.FireLight, PColor.FireDark)
            "water" -> Pair(PColor.WaterLight, PColor.WaterDark)
            "electric" -> Pair(PColor.ElectricLight, PColor.ElectricDark)
            else -> Pair(PColor.GreyLight, PColor.GreyDark)
        }
}