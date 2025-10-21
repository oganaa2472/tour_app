package com.example.survey.feature.login.data.local


import android.content.Context
import androidx.datastore.preferences.core.edit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(
    private val context: Context
) {
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[DataStoreKeys.KEY_TOKEN] = token
        }
    }

    // Get token as Flow
    val authToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[DataStoreKeys.KEY_TOKEN]
    }

    // Clear token
    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(DataStoreKeys.KEY_TOKEN)
        }
    }
}