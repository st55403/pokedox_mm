package eu.golovkov.feature.pokemonfilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.golovkov.core.network.ktor.ApiService
import eu.golovkov.core.network.model.TypeResponse
import eu.golovkov.core.ui.StatefulLayoutState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonFilterViewModel(
    private val apiService: ApiService,
) : ViewModel(), PokemonFilterStateHolder {
    private val mutableState: MutableStateFlow<PokemonFilterStateHolder.State> =
        MutableStateFlow(PokemonFilterStateHolder.State.Data())
    override val state: StateFlow<PokemonFilterStateHolder.State> = mutableState.asStateFlow()

    fun loadTypes() {
        mutableState.value = PokemonFilterStateHolder.State.Loading
        viewModelScope.launch {
            mutableState.value = try {
                val types = apiService.getPokemonTypes()
                PokemonFilterStateHolder.State.Data(
                    typesOrGenerations = types
                )
            } catch (e: Exception) {
                PokemonFilterStateHolder.State.Message.Error(e.message)
            }
        }
    }

    fun loadGenerations() {
        mutableState.value = PokemonFilterStateHolder.State.Loading
        viewModelScope.launch {
            mutableState.value = try {
                val generation = apiService.getPokemonGenerations()
                PokemonFilterStateHolder.State.Data(
                    typesOrGenerations = generation
                )
            } catch (e: Exception) {
                PokemonFilterStateHolder.State.Message.Error(e.message)
            }
        }
    }
}

interface PokemonFilterStateHolder : PokemonFilterStateTransformer {
    sealed interface State : StatefulLayoutState<State.Data, State.Message, State.Loading> {
        data class Data(
            val typesOrGenerations: TypeResponse? = null,
        ) : State, StatefulLayoutState.Data

        sealed interface Message : State, StatefulLayoutState.Message {
            data class Error(val message: String?) : Message
        }

        object Loading : State, StatefulLayoutState.Loading
    }

    val state: StateFlow<State>
}

interface PokemonFilterStateTransformer {
    fun itemSelected() {}
}
