package eu.golovkov.core.database.repository

import eu.golovkov.core.database.dao.PokemonDao
import eu.golovkov.core.database.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

typealias Pokemons = List<PokemonEntity>

interface PokemonRepository {
    fun getAll(): Flow<Pokemons>
    fun getById(id: Int): Flow<PokemonEntity>
    fun addPokemon(pokemon: PokemonEntity)
    fun removePokemon(pokemon: PokemonEntity)
}

class PokemonRepositoryImpl(
    private val pokemonDao: PokemonDao
) : PokemonRepository {
    override fun getAll(): Flow<Pokemons> =
        pokemonDao.getAll()

    override fun getById(id: Int): Flow<PokemonEntity> =
        pokemonDao.getById(id)

    override fun addPokemon(pokemon: PokemonEntity) =
        pokemonDao.addPokemon(pokemon)

    override fun removePokemon(pokemon: PokemonEntity) =
        pokemonDao.removePokemon(pokemon)
}