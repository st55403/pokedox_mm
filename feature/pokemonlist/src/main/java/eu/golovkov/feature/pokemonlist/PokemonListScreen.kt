package eu.golovkov.feature.pokemonlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import eu.golovkov.core.ui.StatefulLayout
import eu.golovkov.core.ui.asData
import eu.golovkov.core.ui.theme.PPadding
import eu.golovkov.feature.pokemondetails.destinations.PokemonDetailsScreenDestination
import eu.golovkov.feature.pokemonlist.model.Pokemon
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun PokemonListScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = getViewModel<PokemonListViewModel>()

    PokemonList(
        stateHolder = viewModel,
        onPokemonClick = { pokemon ->
            navigator.navigate(
                PokemonDetailsScreenDestination(
                    pokemonName = pokemon.name
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonList(
    stateHolder: PokemonListStateHolder,
    onPokemonClick: (Pokemon) -> Unit = {},
) {
    val state = stateHolder.state.collectAsState().value
    val pokemons = state.asData()?.pokemons?.collectAsLazyPagingItems() ?: return

    // TODO: move in vm
    val selectedChips = remember {
        mutableStateListOf<String>()
    }
    val chipItems = listOf("Favourite Pokemon", "All Type", "All Gen")

    LaunchedEffect(pokemons.loadState, pokemons.itemCount) {
        stateHolder.onLoadStatesChanged(pokemons.loadState, pokemons.itemCount)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.pokemon_list_title))
                    }
                )
                Row {
                    chipItems.forEach { chipItem ->
                        val isSelected = selectedChips.contains(chipItem)
                        FilterChip(
                            label = { Text(chipItem) },
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedChips.remove(chipItem)
                                } else {
                                    selectedChips.add(chipItem)
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        StatefulLayout(
            state = state,
            message = { message ->
                when (message) {
                    is PokemonListStateHolder.State.Message.Error -> {
                        Text(
                            text = message.message ?: stringResource(R.string.generic_error_message),
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            },
            modifier = Modifier.padding(it)
        ) {
            if (pokemons.loadState.refresh is LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center)
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                for (index in 0 until pokemons.itemCount) {
                    when (pokemons.peek(index)) {
                        is Pokemon -> {
                            item {
                                val pokemon = pokemons[index] as Pokemon
                                PokemonItem(
                                    pokemon = pokemon,
                                    onPokemonClick = { onPokemonClick(pokemon) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onPokemonClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .clickable { onPokemonClick() }
            .fillMaxHeight()
            .padding(PPadding.medium)
    ) {
        Text(text = pokemon.id.toString())
        Text(text = pokemon.name)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.imageUrl)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = null,
            modifier = Modifier
                .padding(PPadding.medium)
                .size(PPadding.huge),
            contentScale = ContentScale.Fit,
        )
        Divider()
    }
}
