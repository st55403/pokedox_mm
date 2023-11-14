package eu.golovkov.core.datastore.di

import eu.golovkov.core.datastore.DataStorePreferenceRepository
import org.koin.core.module.Module
import org.koin.dsl.module

object DataStoreModule {
    operator fun invoke(): Module = module {
        single {
            DataStorePreferenceRepository(
                context = get()
            )
        }
    }
}