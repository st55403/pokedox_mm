package eu.golovkov.feature.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.golovkov.core.data.Pokemon
import eu.golovkov.core.data.Stat
import eu.golovkov.core.database.repository.PokemonRepository
import eu.golovkov.core.network.ktor.ApiService
import eu.golovkov.core.ui.CardBackgroundColor
import eu.golovkov.core.ui.StatefulLayoutState
import eu.golovkov.core.ui.asData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val apiService: ApiService,
    private val pokemonRepository: PokemonRepository
) : ViewModel(), PokemonListStateHolder {
    private val mutableState: MutableStateFlow<PokemonListStateHolder.State> =
        MutableStateFlow(PokemonListStateHolder.State.Loading)
    override val state: StateFlow<PokemonListStateHolder.State> = mutableState.asStateFlow()

    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            mutableState.value = try {
                val result = Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = { PokemonSource(apiService = apiService) },
                ).flow.cachedIn(viewModelScope)
                PokemonListStateHolder.State.Data(
                    pokemons = result,
                )
            } catch (e: Exception) {
                PokemonListStateHolder.State.Message.Error(e.message)
            }
        }
    }

    override fun getPokemonsByType(typeName: String) {
        val dataState = state.value.asData() ?: return
        viewModelScope.launch {
            try {
                val pokemonsByType = apiService.getPokemonListByType(typeName)
                    .pokemon.take(10).map {
                        async {
                            apiService.getPokemonDetails(it.pokemon.name)
                        }
                    }.awaitAll()
                val pokemonList = pokemonsByType.map { pokemon ->
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
                mutableState.value = dataState.copy(
                    pokemons = flowOf(PagingData.from(pokemonList)),
                )
            } catch (e: Exception) {
                mutableState.value = PokemonListStateHolder.State.Message.Error(e.message)
            }
        }
    }

    override fun getPokemonsByGeneration(generationName: String) {
        val dataState = state.value.asData() ?: return
        viewModelScope.launch {
            try {
                val pokemonsByGeneration = apiService.getPokemonListByGeneration(generationName)
                    .pokemonList.take(10).map {
                        async {
                            apiService.getPokemonDetails(it.name)
                        }
                    }.awaitAll()
                val pokemonList = pokemonsByGeneration.map { pokemon ->
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
                mutableState.value = dataState.copy(
                    pokemons = flowOf(PagingData.from(pokemonList)),
                )
            } catch (e: Exception) {
                mutableState.value = PokemonListStateHolder.State.Message.Error(e.message)
            }
        }
    }
}

interface PokemonListStateHolder : PokemonListStateTransformer {
    sealed interface State : StatefulLayoutState<State.Data, State.Message, State.Loading> {
        data class Data(
            val pokemons: Flow<PagingData<Pokemon>>,
        ) : State, StatefulLayoutState.Data

        sealed interface Message : State, StatefulLayoutState.Message {
            data class Error(val message: String?) : Message
        }

        object Loading : State, StatefulLayoutState.Loading
    }

    val state: StateFlow<State>
}

interface PokemonListStateTransformer {
    fun onLoadStatesChanged(loadStates: CombinedLoadStates, itemCount: Int) {}
    fun getPokemonsByType(typeName: String) {}
    fun getPokemonsByGeneration(generationName: String) {}
}
