package eu.golovkov.feature.pokemondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import eu.golovkov.core.ui.StatefulLayout
import eu.golovkov.core.ui.asData
import eu.golovkov.core.ui.sequentialId
import eu.golovkov.core.ui.theme.PColor
import eu.golovkov.core.ui.theme.PPadding
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun PokemonDetailsScreen(
    pokemonName: String? = null,
) {
    val viewModel = getViewModel<PokemonDetailsViewModel>()

    LaunchedEffect(pokemonName) {
        pokemonName?.let {
            viewModel.loadPokemon(it)
        }
    }

    PokemonDetails(
        stateHolder = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonDetails(
    stateHolder: PokemonDetailsViewModel,
) {
    val state = stateHolder.state.collectAsState().value
    val isFavorite = state.asData()?.isFavorite ?: false
    val backgroundColor =
        state.asData()?.pokemon?.color?.second ?: MaterialTheme.colorScheme.background

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = stateHolder::changeFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = backgroundColor,
                    navigationIconContentColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) {
        StatefulLayout(
            state = state,
            message = { message ->
                when (message) {
                    is PokemonDetailsStateHolder.State.Message.Error -> {
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
            val pokemon = state.asData()?.pokemon ?: return@StatefulLayout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PPadding.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pokemon.name.capitalize(Locale.current),
                        modifier = Modifier.alignByBaseline(),
                        color = PColor.White,
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = pokemon.id.sequentialId(),
                        modifier = Modifier.alignByBaseline(),
                        color = PColor.White,
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                Row {
                    pokemon.types.forEach { type ->
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = PPadding.medium,
                                    top = PPadding.medium,
                                )
                                .background(
                                    color = pokemon.color.first,
                                    shape = MaterialTheme.shapes.medium
                                )
                        ) {
                            Text(
                                text = type.capitalize(Locale.current),
                                modifier = Modifier
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
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pokemon.imageUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = PPadding.big)
                        .padding(horizontal = PPadding.huge)
                        .padding(horizontal = PPadding.big),
                    contentScale = ContentScale.FillWidth,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(
                                topStart = PPadding.bigger,
                                topEnd = PPadding.bigger,
                            )
                        )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PPadding.medium),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = PPadding.tiny
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = PPadding.small),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.height_lable),
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        color = PColor.GreyTitle
                                    )
                                )
                                Text(
                                    text = pokemon.height.toString()
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(vertical = PPadding.small),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.weight_lable),
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        color = PColor.GreyTitle
                                    )
                                )
                                Text(
                                    text = pokemon.weight.toString()
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(PPadding.medium)
                    ) {
                        Text(
                            text = stringResource(R.string.training_lable),
                            style = MaterialTheme.typography.displayMedium
                        )
                        pokemon.stats.forEach { stat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(PPadding.medium)
                            ) {
                                Text(
                                    text = stat.stat,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        color = PColor.GreyTitle
                                    ),
                                )
                                Text(
                                    text = stat.value.toString(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}