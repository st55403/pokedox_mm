package eu.golovkov.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListByGenerationResponse(
    @SerialName("pokemon_species")
    val pokemonList: List<Result>,
)