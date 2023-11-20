package eu.golovkov.feature.pokemonfilter

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import eu.golovkov.core.network.model.TypeResponse
import eu.golovkov.core.ui.StatefulLayout
import eu.golovkov.core.ui.asData
import eu.golovkov.core.ui.theme.PPadding
import org.koin.androidx.compose.getViewModel
import eu.golovkov.core.ui.R as UI_R

@Composable
fun PokemonFilterScreen(
    bottomSheetType: String = "type",
    onItemClicked: (String) -> Unit,
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
        isType = bottomSheetType == "type",
        stateHolder = viewModel,
        onItemClicked = onItemClicked
    )
}

@Composable
private fun PokemonFilter(
    isType: Boolean,
    stateHolder: PokemonFilterStateHolder,
    onItemClicked: (String) -> Unit,
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
        state.asData()?.typesOrGenerations?.let { type ->
            BottomSheetContent(
                type = type,
                titleRes = if (isType) R.string.type_title else R.string.generation_title,
                onItemClicked = onItemClicked
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    type: TypeResponse,
    titleRes: Int,
    modifier: Modifier = Modifier,
    onItemClicked: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(titleRes),
            modifier = modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                horizontal = PPadding.medium,
                vertical = PPadding.medium,
            )
        ) {
            for (items in type.results) {
                item {
                    GridItem(
                        modifier = modifier
                            .clickable { onItemClicked(items.name) },
                        title = items.name.capitalize(Locale.current),
                    )
                }
            }
        }
    }
}

@Composable
private fun GridItem(
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(PPadding.small),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = PPadding.tiny
        )
    ) {
        ConstraintLayout {
            val (name, image, background) = createRefs()
            Text(
                text = title,
                modifier = modifier
                    .constrainAs(name) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(top = PPadding.medium),
                style = MaterialTheme.typography.displaySmall
            )
            Image(
                painter = painterResource(UI_R.drawable.ic_ball_background),
                contentDescription = null,
                modifier = modifier.constrainAs(background) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}
