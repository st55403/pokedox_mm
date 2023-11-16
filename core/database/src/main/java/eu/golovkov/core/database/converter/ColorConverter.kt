package eu.golovkov.core.database.converter

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class ColorConverter {
    @TypeConverter
    fun fromPair(pair: Pair<Color, Color>): String {
        return "${pair.first.value},${pair.second.value}"
    }

    @TypeConverter
    fun toPair(value: String): Pair<Color, Color> {
        val (color1, color2) = value.split(",")
        return Pair(Color(color1.toInt()), Color(color2.toInt()))
    }
}