package eu.golovkov.feature.pokemonlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import eu.golovkov.core.data.Pokemon
import eu.golovkov.core.data.Stat
import eu.golovkov.core.network.ktor.ApiService
import eu.golovkov.core.ui.CardBackgroundColor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PokemonSource(
    private val apiService: ApiService,
) : PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val page = params.key ?: 1
        val offset = if (page == 1) 0 else (page - 1) * 20

        return try {
            val pokemonsOnPage = apiService.getPokemonList(offset = offset)

            val pokemonsDetailedInfo = coroutineScope {
                pokemonsOnPage.results.map { pokemonListItem ->
                    async {
                        apiService.getPokemonDetails(pokemonListItem.name)
                    }
                }.awaitAll()
            }

            val pokemonList = pokemonsDetailedInfo.map { pokemon ->
                Pokemon(
                    id = pokemon.id,
                    name = pokemon.name,
                    imageUrl = pokemon.sprites.other.dreamWorld.frontDefault,
                    color = CardBackgroundColor.getColor(pokemon.types.first().type.name),
                    types = pokemon.types.map { it.type.name },
                    height = pokemon.height,
                    weight = pokemon.weight,
                    stats = pokemon.stats.map { Stat(it.baseStat, it.stat.name) }
                )
            }

            LoadResult.Page(
                data = pokemonList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page == pokemonsOnPage.count) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}