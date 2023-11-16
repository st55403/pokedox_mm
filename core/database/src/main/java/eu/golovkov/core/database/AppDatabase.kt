package eu.golovkov.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.golovkov.core.database.dao.PokemonDao
import eu.golovkov.core.database.model.PokemonEntity

@Database(
    entities = [
        PokemonEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}