package eu.golovkov.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TypeResponse(
    val results: List<Result>
)