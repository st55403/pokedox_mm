package eu.golovkov.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import eu.golovkov.core.datastore.di.DataStoreModule
import eu.golovkov.core.navigation.RootNavGraph
import eu.golovkov.core.network.di.NetworkModule
import eu.golovkov.core.ui.theme.PTheme
import eu.golovkov.feature.pokemondetails.di.PokemonDetailsModule
import eu.golovkov.feature.pokemonlist.di.PokemonListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity.application)
            modules(
                NetworkModule(),
                PokemonListModule(),
                PokemonDetailsModule(),
                DataStoreModule()
            )
        }

        setContent {
            PTheme {
                DestinationsNavHost(
                    navGraph = RootNavGraph
                )
            }
        }
    }
}
