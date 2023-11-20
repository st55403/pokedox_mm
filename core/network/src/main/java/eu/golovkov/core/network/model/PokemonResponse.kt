package eu.golovkov.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val stats: List<Stat>,
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
    val frontDefault: String? = null,
)

@Serializable
data class Type(
    val type: TypeItem,
)

@Serializable
data class TypeItem(
    val name: String,
)

@Serializable
data class Stat(
    @SerialName("base_stat")
    val baseStat: Int,
    val stat: StatItem,
)

@Serializable
data class StatItem(
    val name: String,
)
