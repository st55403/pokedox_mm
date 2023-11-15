package eu.golovkov.core.data

import androidx.compose.ui.graphics.Color

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val color: Pair<Color, Color>,
    val types: List<String>,
)