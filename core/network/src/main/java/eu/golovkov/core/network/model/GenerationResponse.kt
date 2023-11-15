package eu.golovkov.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerationResponse(
    val results: List<Result>
)