package com.nochunsam.makeyourmorning.utilities.pref

import android.content.Context
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PrefDataStore(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "MakeYourMorning")

    private object PrefKeys {
        val FIRST_OPEN = booleanPreferencesKey("first_open")
    }

    // 읽기
    val isFirstOpen: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            prefs[PrefKeys.FIRST_OPEN] ?: true
        }

    // 쓰기
    suspend fun setFirstOpen() {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.FIRST_OPEN] = false
        }
    }
}