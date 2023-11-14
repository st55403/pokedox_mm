package eu.golovkov.core.network.di

import eu.golovkov.core.network.ktor.ApiService
import org.koin.core.module.Module
import org.koin.dsl.module

private const val BASE_URL = "https://pokeapi.co/api/v2/"

object NetworkModule {
    operator fun invoke(): Module = module {
        single<ApiService> {
            ApiService.create(BASE_URL)
        }
    }
}