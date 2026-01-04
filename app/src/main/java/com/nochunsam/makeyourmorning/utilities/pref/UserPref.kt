package com.nochunsam.makeyourmorning.utilities.pref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

object PrefKeys {
    val FIRST_OPEN = booleanPreferencesKey("first_open")
}

class FirstOpenManager(private val context: Context) {

    // Read
    val isFirstOpen: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.FIRST_OPEN] ?: true
    }

    // Write
    suspend fun setFirstOpen() {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.FIRST_OPEN] = false
        }
    }
}

suspend fun Context.readFirstOpenOnce(): Boolean {
    return dataStore.data.first()[PrefKeys.FIRST_OPEN] ?: true
}