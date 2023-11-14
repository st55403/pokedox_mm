package eu.golovkov.feature.pokemondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eu.golovkov.core.network.ktor.ApiService
import eu.golovkov.core.network.model.PokemonResponse
import eu.golovkov.core.ui.StatefulLayoutState
import eu.golovkov.core.ui.asData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    private val apiService: ApiService,
) : ViewModel(), PokemonDetailsStateHolder {
    private val mutableState: MutableStateFlow<PokemonDetailsStateHolder.State> =
        MutableStateFlow(PokemonDetailsStateHolder.State.Loading)
    override val state: StateFlow<PokemonDetailsStateHolder.State> = mutableState.asStateFlow()

    fun loadPokemon(pokemonName: String) {
        viewModelScope.launch {
            mutableState.value = try {
                val pokemon = apiService.getPokemonDetails(pokemonName)
                PokemonDetailsStateHolder.State.Data(
                    pokemon
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

            } catch (e: Exception) {
                mutableState.value = PokemonDetailsStateHolder.State.Message.Error(e.message)
            }
        }
    }
}

interface PokemonDetailsStateHolder : PokemonDetailsStateTransformer {
    sealed interface State : StatefulLayoutState<State.Data, State.Message, State.Loading> {
        data class Data(
            val pokemon: PokemonResponse? = null,
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
