package eu.golovkov.feature.pokemonfilter.di

import eu.golovkov.feature.pokemonfilter.PokemonFilterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object PokemonFilterModule {
    operator fun invoke(): Module = module {
        viewModel {
            PokemonFilterViewModel(
                apiService = get(),
            )
        }
    }
}