package eu.golovkov.core.database.di

import androidx.room.Room
import eu.golovkov.core.database.AppDatabase
import eu.golovkov.core.database.repository.PokemonRepository
import eu.golovkov.core.database.repository.PokemonRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private const val APP_DATABASE_NAME = "app_database"

object DatabaseModule {
    operator fun invoke(): Module = module {
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                APP_DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        single<PokemonRepository> {
            val pokemonDao = get<AppDatabase>().pokemonDao()
            PokemonRepositoryImpl(pokemonDao)
        }
    }
}