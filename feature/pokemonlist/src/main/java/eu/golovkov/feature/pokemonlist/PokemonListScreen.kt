package eu.golovkov.feature.pokemonlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import eu.golovkov.core.ui.StatefulLayout
import eu.golovkov.core.ui.asData
import eu.golovkov.feature.pokemonlist.model.Pokemon
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun PokemonListScreen() {
    val viewModel = getViewModel<PokemonListViewModel>()

    PokemonList(
        stateHolder = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonList(
    stateHolder: PokemonListStateHolder,
) {
    val state = stateHolder.state.collectAsState().value
    val pokemons = state.asData()?.pokemons?.collectAsLazyPagingItems() ?: return

    LaunchedEffect(pokemons.loadState, pokemons.itemCount) {
        stateHolder.onLoadStatesChanged(pokemons.loadState, pokemons.itemCount)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.pokemon_list_title))
                }
            )
        }
    ) {
        StatefulLayout(
            state = state,
            message = { message ->
                when (message) {
                    is PokemonListStateHolder.State.Message.Error -> {
                        Text(
                            text = message.message ?: "Unknown error",
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
                                val character = pokemons[index] as Pokemon
                                PokemonItem(
                                    pokemon = character,
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
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
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
                .padding(16.dp)
                .size(64.dp),
            contentScale = ContentScale.Fit,
        )
        Divider()
    }
}
