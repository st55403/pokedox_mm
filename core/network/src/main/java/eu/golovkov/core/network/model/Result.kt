package eu.golovkov.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val name: String,
    val url: String
)
