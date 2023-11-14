package eu.golovkov.feature.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import eu.golovkov.core.network.ktor.ApiService
import eu.golovkov.core.network.model.PokemonListItem
import eu.golovkov.core.ui.StatefulLayoutState
import eu.golovkov.feature.pokemonlist.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val apiService: ApiService,
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
}
