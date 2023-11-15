package eu.golovkov.feature.pokemonfilter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.golovkov.core.ui.StatefulLayout
import eu.golovkov.core.ui.asData
import org.koin.androidx.compose.getViewModel

@Composable
fun PokemonFilterScreen(
    bottomSheetType: String = "type"
) {
    val viewModel = getViewModel<PokemonFilterViewModel>()

    LaunchedEffect(bottomSheetType) {
        bottomSheetType.let {
            when (it) {
                "type" -> viewModel.loadTypes()
                "generation" -> viewModel.loadGenerations()
            }
        }
    }

    PokemonFilter(
        stateHolder = viewModel,
    )
}

@Composable
private fun PokemonFilter(
    stateHolder: PokemonFilterStateHolder
) {
    val state = stateHolder.state.collectAsState().value

    StatefulLayout(
        state = state,
        message = { message ->
            when (message) {
                is PokemonFilterStateHolder.State.Message.Error -> {
                    Text(
                        text = message.message ?: stringResource(R.string.generic_error_message),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    ) {
        state.asData()?.type?.let {
            Column {
                Text(
                    text = stringResource(R.string.type_title)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                ) {
                    for (type in it.results) {
                        item {
                            Text(
                                text = type.name
                            )
                        }
                    }
                }
            }
        }

        state.asData()?.generation?.let {
            Column {
                Text(
                    text = stringResource(R.string.generation_title)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                ) {
                    for (generation in it.results) {
                        item {
                            Text(
                                text = generation.name
                            )
                        }
                    }
                }
            }
        }
    }
}
