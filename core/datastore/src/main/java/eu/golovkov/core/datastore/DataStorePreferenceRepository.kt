package eu.golovkov.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStorePreferenceRepository(
    context: Context,
) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    private val favoritePokemonNames = stringSetPreferencesKey("favorite_pokemon_names")

    fun getNames() = dataStore.data.map { preferences ->
        preferences[favoritePokemonNames] ?: emptySet()
    }

    suspend fun updateNames(name: String) {
        dataStore.edit { preferences ->
            val currentNames = preferences[favoritePokemonNames] ?: emptySet()
            val updatedNames = currentNames.let { names ->
                if (names.contains(name)) names - name else names + name
            }
            preferences[favoritePokemonNames] = updatedNames
        }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")