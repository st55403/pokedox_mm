package eu.golovkov.feature.pokemondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.golovkov.core.data.Pokemon
import eu.golovkov.core.data.Stat
import eu.golovkov.core.database.model.PokemonEntity
import eu.golovkov.core.database.repository.PokemonRepository
import eu.golovkov.core.network.ktor.ApiService
import eu.golovkov.core.ui.CardBackgroundColor
import eu.golovkov.core.ui.StatefulLayoutState
import eu.golovkov.core.ui.asData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonDetailsViewModel(
    private val apiService: ApiService,
    private val pokemonRepository: PokemonRepository,
) : ViewModel(), PokemonDetailsStateHolder {
    private val mutableState: MutableStateFlow<PokemonDetailsStateHolder.State> =
        MutableStateFlow(PokemonDetailsStateHolder.State.Loading)
    override val state: StateFlow<PokemonDetailsStateHolder.State> = mutableState.asStateFlow()

    fun loadPokemon(pokemonName: String) {
        viewModelScope.launch {
            mutableState.value = try {
                val response = apiService.getPokemonDetails(pokemonName)
                val pokemon = Pokemon(
                    id = response.id,
                    name = response.name,
                    imageUrl = response.sprites.other.dreamWorld.frontDefault,
                    color = CardBackgroundColor.getColor(response.types.first().type.name),
                    types = response.types.map { it.type.name },
                    height = response.height,
                    weight = response.weight,
                    stats = response.stats.map { Stat(it.baseStat, it.stat.name) }
                )
                val isFavorite = withContext(Dispatchers.IO) {
                    pokemonRepository.isFavorite(pokemon.id)
                }
                PokemonDetailsStateHolder.State.Data(
                    pokemon = pokemon,
                    isFavorite = isFavorite
                )
            } catch (e: Exception) {
                PokemonDetailsStateHolder.State.Message.Error(e.message)
            }
        }
    }

    override fun changeFavorite() {
        val dataState = state.value.asData() ?: return
        viewModelScope.launch {
            try {
                val currentPokemon = dataState.pokemon ?: return@launch
                if (dataState.isFavorite) {
                    withContext(Dispatchers.IO) {
                        pokemonRepository.removePokemon(currentPokemon.id)
                    }
                } else {
                    withContext(Dispatchers.IO) {
                        pokemonRepository.addPokemon(
                            PokemonEntity(
                                id = currentPokemon.id,
                                name = currentPokemon.name,
                                imageUrl = currentPokemon.imageUrl,
                                color = currentPokemon.color,
                                types = currentPokemon.types,
                                height = currentPokemon.height,
                                weight = currentPokemon.weight,
                            )
                        )
                    }
                }
                mutableState.value = dataState.copy(
                    isFavorite = !dataState.isFavorite,
                    wasChanged = true,
                )
            } catch (e: Exception) {
                mutableState.value = PokemonDetailsStateHolder.State.Message.Error(e.message)
            }
        }
    }
}

interface PokemonDetailsStateHolder : PokemonDetailsStateTransformer {
    sealed interface State : StatefulLayoutState<State.Data, State.Message, State.Loading> {
        data class Data(
            val pokemon: Pokemon? = null,
            val isFavorite: Boolean = false,
            val wasChanged: Boolean = false,
        ) : State, StatefulLayoutState.Data

        sealed interface Message : State, StatefulLayoutState.Message {
            data class Error(val message: String?) : Message
        }

        object Loading : State, StatefulLayoutState.Loading
    }

    val state: StateFlow<State>
}

interface PokemonDetailsStateTransformer {
    fun changeFavorite() {}
}
