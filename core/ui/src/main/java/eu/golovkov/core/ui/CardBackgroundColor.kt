package eu.golovkov.core.ui

import androidx.compose.ui.graphics.Color
import eu.golovkov.core.ui.theme.PColor

object CardBackgroundColor {
    fun getColor(name: String): Pair<Color, Color> =
        when (name) {
            "grass" -> Pair(PColor.grassLight, PColor.grassDark)
            "fire" -> Pair(PColor.fireLight, PColor.fireDark)
            "water" -> Pair(PColor.waterLight, PColor.waterDark)
            "electric" -> Pair(PColor.electricLight, PColor.electricDark)
            else -> Pair(PColor.greyLight, PColor.greyDark)
        }
}