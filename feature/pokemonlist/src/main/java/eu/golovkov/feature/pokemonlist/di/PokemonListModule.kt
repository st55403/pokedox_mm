package eu.golovkov.feature.pokemonlist.di

import eu.golovkov.feature.pokemonlist.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object PokemonListModule {
    operator fun invoke(): Module = module {
        viewModel {
            PokemonListViewModel(
                apiService = get(),
                pokemonRepository = get()
            )
        }
    }
}