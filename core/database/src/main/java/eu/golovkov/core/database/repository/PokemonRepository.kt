package eu.golovkov.core.database.repository

import eu.golovkov.core.database.dao.PokemonDao
import eu.golovkov.core.database.model.Pokemon
import kotlinx.coroutines.flow.Flow

typealias Pokemons = List<Pokemon>

interface PokemonRepository {
    fun getAll(): Flow<Pokemons>
    fun getById(id: Int): Flow<Pokemon>
    fun addPokemon(pokemon: Pokemon)
    fun removePokemon(pokemon: Pokemon)
}

class PokemonRepositoryImpl(
    private val pokemonDao: PokemonDao
) : PokemonRepository {
    override fun getAll(): Flow<Pokemons> =
        pokemonDao.getAll()

    override fun getById(id: Int): Flow<Pokemon> =
        pokemonDao.getById(id)

    override fun addPokemon(pokemon: Pokemon) =
        pokemonDao.addPokemon(pokemon)

    override fun removePokemon(pokemon: Pokemon) =
        pokemonDao.removePokemon(pokemon)
}