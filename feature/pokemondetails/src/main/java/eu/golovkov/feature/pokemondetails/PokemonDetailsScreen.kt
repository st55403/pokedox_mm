package eu.golovkov.feature.pokemondetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import eu.golovkov.core.ui.StatefulLayout
import eu.golovkov.core.ui.asData
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
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.AddCircle,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        StatefulLayout(
            state = state,
            message = { message ->
                when (message) {
                    is PokemonDetailsStateHolder.State.Message.Error -> {
                        Text(
                            text = message.message ?: stringResource(R.string.generic_error_message),
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
            ) {
                Row {
                    Text(
                        text = pokemon.name,
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = pokemon.id.toString()
                    )
                }
                Row {
                    pokemon.types.forEach { type ->
                        Text(
                            text = type.type.name
                        )
                    }
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pokemon.sprites.other.dreamWorld.frontDefault)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(PPadding.medium)
                        .size(PPadding.huge),
                    contentScale = ContentScale.Fit,
                )
                Row {
                    Column {
                        Text(
                            text = "Height"
                        )
                        Text(
                            text = pokemon.height.toString()
                        )
                    }
                    Column {
                        Text(
                            text = "Weight"
                        )
                        Text(
                            text = pokemon.weight.toString()
                        )
                    }
                }
                pokemon.stats.forEach { stat ->
                    Row {
                        Text(
                            text = stat.stat.name
                        )
                        Text(
                            text = stat.baseStat.toString()
                        )
                    }
                }
            }
        }
    }
}