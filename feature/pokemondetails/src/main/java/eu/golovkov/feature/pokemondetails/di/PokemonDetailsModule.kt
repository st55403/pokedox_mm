package eu.golovkov.feature.pokemondetails.di

import eu.golovkov.feature.pokemondetails.PokemonDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object PokemonDetailsModule {
    operator fun invoke(): Module = module {
        viewModel {
            PokemonDetailsViewModel(
                apiService = get(),
                dataStorePreference = get()
            )
        }
    }
}