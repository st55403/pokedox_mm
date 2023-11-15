package eu.golovkov.feature.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.constraintlayout.compose.ConstraintLayout
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
import eu.golovkov.core.ui.sequentialId
import eu.golovkov.core.ui.theme.PColor
import eu.golovkov.core.ui.theme.PPadding
import eu.golovkov.feature.pokemondetails.destinations.PokemonDetailsScreenDestination
import eu.golovkov.feature.pokemonfilter.PokemonFilterScreen
import eu.golovkov.feature.pokemonlist.model.Pokemon
import org.koin.androidx.compose.getViewModel
import eu.golovkov.core.ui.R as UI_R

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
    val sheetState = rememberModalBottomSheetState()
    var showTypeBottomSheet by remember { mutableStateOf(false) }
    var showGenerationBottomSheet by remember { mutableStateOf(false) }

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
                        Text(
                            text = stringResource(R.string.pokemon_list_title),
                            modifier = Modifier
                                .padding(start = PPadding.small),
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PPadding.medium)
                        .padding(horizontal = PPadding.small),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        label = { Text("Favorite") },
                        selected = false,
                        onClick = {

                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = UI_R.drawable.ic_heart),
                                contentDescription = null,
                            )
                        }
                    )
                    FilterChip(
                        label = { Text("All Type") },
                        selected = false,
                        onClick = {
                            showTypeBottomSheet = true
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = UI_R.drawable.ic_ball),
                                contentDescription = null,
                            )
                        }
                    )
                    FilterChip(
                        label = { Text("All Gen") },
                        selected = false,
                        onClick = {
                            showGenerationBottomSheet = true
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = UI_R.drawable.ic_ball),
                                contentDescription = null,
                            )
                        }
                    )
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
                            text = message.message
                                ?: stringResource(R.string.generic_error_message),
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
                contentPadding = PaddingValues(
                    horizontal = PPadding.medium,
                    vertical = PPadding.medium,
                )
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
            if (showTypeBottomSheet || showGenerationBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        if (showTypeBottomSheet) {
                            showTypeBottomSheet = false
                        } else {
                            showGenerationBottomSheet = false
                        }
                    },
                    sheetState = sheetState,
                ) {
                    when {
                        showTypeBottomSheet -> {
                            PokemonFilterScreen()
                        }

                        showGenerationBottomSheet -> {
                            PokemonFilterScreen(
                                bottomSheetType = "generation"
                            )
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
    modifier: Modifier = Modifier,
    onPokemonClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .padding(PPadding.small)
            .clickable { onPokemonClick() },
        colors = CardDefaults.cardColors(
            containerColor = pokemon.color.second
        ),
    ) {
        ConstraintLayout {
            val (name, number, image, background, types) = createRefs()
            Image(
                painter = painterResource(id = UI_R.drawable.ic_ball_background),
                contentDescription = null,
                modifier = modifier.constrainAs(background) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.imageUrl)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = null,
                modifier = modifier
                    .constrainAs(image) {
                        end.linkTo(parent.end)
                        top.linkTo(name.bottom)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(bottom = PPadding.small)
                    .size(PPadding.huge),
                contentScale = ContentScale.Fit,
            )
            Text(
                text = pokemon.id.sequentialId(),
                modifier = modifier
                    .constrainAs(number) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(top = PPadding.small),
                style = MaterialTheme.typography.displayMedium
                    .copy(color = LocalContentColor.current.copy(alpha = 0.2f))
            )
            Text(
                text = pokemon.name.capitalize(Locale.current),
                modifier = modifier
                    .constrainAs(name) {
                        start.linkTo(parent.start)
                        top.linkTo(number.bottom)
                    }
                    .padding(start = PPadding.medium,)
                    .padding(vertical = PPadding.small),
                color = PColor.White,
                style = MaterialTheme.typography.displayMedium
            )
            Column(
                modifier = modifier
                    .constrainAs(types) {
                        start.linkTo(parent.start)
                        top.linkTo(name.bottom)
                    },
                verticalArrangement = Arrangement.spacedBy(PPadding.small)
            ) {
                pokemon.types.forEach {
                    Box(
                        modifier = modifier
                            .padding(
                                start = PPadding.medium,
                            )
                            .background(
                                color = pokemon.color.first,
                                shape = MaterialTheme.shapes.medium
                            )
                    ) {
                        Text(
                            text = it.capitalize(Locale.current),
                            modifier = modifier
                                .padding(
                                    vertical = PPadding.tiny,
                                    horizontal = PPadding.small
                                ),
                            color = PColor.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
