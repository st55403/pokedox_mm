package eu.golovkov.feature.pokemondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.golovkov.core.data.Pokemon
import eu.golovkov.core.data.Stat
import eu.golovkov.core.datastore.DataStorePreferenceRepository
import eu.golovkov.core.network.ktor.ApiService
import eu.golovkov.core.ui.CardBackgroundColor
import eu.golovkov.core.ui.StatefulLayoutState
import eu.golovkov.core.ui.asData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    private val apiService: ApiService,
    private val dataStorePreference: DataStorePreferenceRepository,
) : ViewModel(), PokemonDetailsStateHolder {
    private val mutableState: MutableStateFlow<PokemonDetailsStateHolder.State> =
        MutableStateFlow(PokemonDetailsStateHolder.State.Loading)
    override val state: StateFlow<PokemonDetailsStateHolder.State> = mutableState.asStateFlow()

    fun loadPokemon(pokemonName: String) {
        viewModelScope.launch {
            mutableState.value = try {
                val isFavorite = dataStorePreference.getNames().first().contains(pokemonName)
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
                dataState.pokemon?.name?.let {
                    dataStorePreference.updateNames(it)
                    mutableState.value = dataState.copy(
                        isFavorite = !dataState.isFavorite,
                        wasChanged = true,
                    )
                }
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
