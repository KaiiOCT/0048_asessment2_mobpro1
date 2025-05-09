package com.faris0048.asessment2.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings_preference"
)

class SettingsDataStore(private val context: Context) {

    companion object {
        private val IS_JURNAL = booleanPreferencesKey("is_jurnal")
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data.map { preference ->
        preference[IS_JURNAL] ?: true
    }

    suspend fun saveLayout(isJurnal: Boolean) {
        context.dataStore.edit { preference ->
            preference[IS_JURNAL] = isJurnal
        }
    }
}