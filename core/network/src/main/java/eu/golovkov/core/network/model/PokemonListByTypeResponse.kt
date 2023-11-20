package eu.golovkov.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListByTypeResponse(
    val pokemon: List<Pokemon>
)

@Serializable
data class Pokemon(
    val pokemon: Result,
)