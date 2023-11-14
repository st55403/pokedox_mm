package eu.golovkov.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val id: Int,
    val name: String,
    val sprites: Sprites,
)

@Serializable
data class Sprites(
    val other: Other,
)

@Serializable
data class Other(
    @SerialName("dream_world")
    val dreamWorld: DreamWorld,
)

@Serializable
data class DreamWorld(
    @SerialName("front_default")
    val frontDefault: String,
)