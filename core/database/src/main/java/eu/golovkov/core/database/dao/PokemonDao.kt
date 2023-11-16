package eu.golovkov.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import eu.golovkov.core.database.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert
    fun addPokemon(pokemonEntity: PokemonEntity)

    @Query("DELETE FROM pokemon WHERE id = :pokemonId")
    fun removePokemon(pokemonId: Int)

    @Query("SELECT * FROM pokemon")
    fun getAll(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getById(id: Int): Flow<PokemonEntity>

    @Query("SELECT EXISTS(SELECT * FROM pokemon WHERE id = :pokemonId)")
    fun isFavorite(pokemonId: Int): Boolean
}