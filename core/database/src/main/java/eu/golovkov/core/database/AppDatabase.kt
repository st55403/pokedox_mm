package eu.golovkov.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.golovkov.core.database.converter.ColorConverter
import eu.golovkov.core.database.converter.StringListConverter
import eu.golovkov.core.database.dao.PokemonDao
import eu.golovkov.core.database.model.Pokemon

@Database(
    entities = [
        Pokemon::class,
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    ColorConverter::class,
    StringListConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}