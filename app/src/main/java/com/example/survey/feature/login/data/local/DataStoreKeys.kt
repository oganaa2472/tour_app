package com.example.survey.feature.login.data.local

import androidx.datastore.preferences.core.stringPreferencesKey
import java.util.prefs.Preferences

object DataStoreKeys
{
    val KEY_TOKEN = stringPreferencesKey("auth_token")
}