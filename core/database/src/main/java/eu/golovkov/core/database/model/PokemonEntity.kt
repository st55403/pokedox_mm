package eu.golovkov.core.database.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import eu.golovkov.core.database.converter.ColorConverter
import eu.golovkov.core.database.converter.StringListConverter

@Entity(tableName = "pokemon")
@TypeConverters(
    ColorConverter::class,
    StringListConverter::class,
)
data class PokemonEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "color") val color: Pair<Color, Color>,
    @ColumnInfo(name = "types") val types: List<String>,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Int,
)
