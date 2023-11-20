package eu.golovkov.core.network.ktor

import eu.golovkov.core.network.model.PokemonListByGenerationResponse
import eu.golovkov.core.network.model.PokemonListByTypeResponse
import eu.golovkov.core.network.model.PokemonListResponse
import eu.golovkov.core.network.model.PokemonResponse
import eu.golovkov.core.network.model.TypeResponse
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

    override suspend fun getPokemonTypes(): TypeResponse =
        client.get("type").body()

    override suspend fun getPokemonListByType(typeName: String): PokemonListByTypeResponse =
        client.get("type/$typeName").body()

    override suspend fun getPokemonGenerations(): TypeResponse =
        client.get("generation").body()

    override suspend fun getPokemonListByGeneration(generationName: String): PokemonListByGenerationResponse =
        client.get("generation/$generationName").body()
}