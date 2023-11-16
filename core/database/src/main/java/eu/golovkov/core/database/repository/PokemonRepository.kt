package eu.golovkov.core.database.repository

import eu.golovkov.core.database.dao.PokemonDao
import eu.golovkov.core.database.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

typealias Pokemons = List<PokemonEntity>

interface PokemonRepository {
    fun getAll(): Flow<Pokemons>
    fun getById(id: Int): Flow<PokemonEntity>
    fun addPokemon(pokemonEntity: PokemonEntity)
    fun removePokemon(pokemonId: Int)
    fun isFavorite(pokemonId: Int): Boolean
}

class PokemonRepositoryImpl(
    private val pokemonDao: PokemonDao
) : PokemonRepository {
    override fun getAll(): Flow<Pokemons> =
        pokemonDao.getAll()

    override fun getById(id: Int): Flow<PokemonEntity> =
        pokemonDao.getById(id)

    override fun addPokemon(pokemonEntity: PokemonEntity) =
        pokemonDao.addPokemon(pokemonEntity)

    override fun removePokemon(pokemonId: Int) =
        pokemonDao.removePokemon(pokemonId)

    override fun isFavorite(pokemonId: Int): Boolean =
        pokemonDao.isFavorite(pokemonId)
}