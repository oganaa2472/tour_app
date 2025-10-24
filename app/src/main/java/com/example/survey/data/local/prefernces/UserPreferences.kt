package com.example.survey.data.local.prefernces


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val TOKEN = stringPreferencesKey("token")
        private val ROLE = stringPreferencesKey("user_role")

        private val PHOTO = stringPreferencesKey("photo")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        private val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userPhoto: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PHOTO]
    }

    val userToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN]
    }

    val notificationEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_ENABLED] ?: true
    }

    val themeMode: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[THEME_MODE]
    }

    suspend fun saveLoginData(
        userId: String,
        email: String,
        username: String,
        token: String,
        role: String,
        photo: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = username
            preferences[TOKEN] = token
            preferences[ROLE] = role
            preferences[PHOTO] = photo
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.remove(IS_LOGGED_IN);
            preferences.remove(USER_ID);
            preferences.remove(USER_EMAIL);
            preferences.remove(USER_NAME);
            preferences.remove(TOKEN);
            preferences.remove(ROLE);
            preferences.remove(PHOTO);

        }
    }

    suspend fun updateAccessToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = accessToken
        }
    }

    suspend fun setNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED] = enabled
        }
    }

    suspend fun setThemeMode(themeMode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }

    suspend fun updateLastSyncTime() {
        context.dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun updateUserPhoto(photoUrl: String) {
        context.dataStore.edit { preferences ->
            preferences[PHOTO] = photoUrl
        }
    }
}