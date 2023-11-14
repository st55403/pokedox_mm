package eu.golovkov.core.network.ktor

import eu.golovkov.core.network.model.PokemonListResponse
import eu.golovkov.core.network.model.PokemonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {
    override suspend fun getPokemonList(offset: Int): PokemonListResponse =
        client.get("pokemon") {
            parameter("offset", offset)
        }.body()

    override suspend fun getPokemonDetails(name: String): PokemonResponse =
        client.get("pokemon/$name").body()
}