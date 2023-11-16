package eu.golovkov.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import eu.golovkov.core.database.model.Pokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert
    fun addPokemon(pokemon: Pokemon)

    @Delete
    fun removePokemon(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon")
    fun getAll(): Flow<List<Pokemon>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getById(id: Int): Flow<Pokemon>
}