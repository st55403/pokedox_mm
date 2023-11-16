package eu.golovkov.core.database.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "color_1") val color1: Color,
    @ColumnInfo(name = "color_2") val color2: Color,
    @ColumnInfo(name = "types") val types: List<String>,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "stats") val stats: List<Stat>
)

@Entity(tableName = "stats")
data class Stat(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "value") val value: Int,
    @ColumnInfo(name = "stat") val stat: String
)
