package com.example.restomaster.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.restomaster.domain.model.AppMode
import com.example.restomaster.domain.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class AppSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val APP_MODE = stringPreferencesKey("app_mode")
        val HOST_IP = stringPreferencesKey("host_ip")
        val HOST_PORT = intPreferencesKey("host_port")
        val GEMINI_API_KEY = stringPreferencesKey("gemini_api_key")
        val OPENAI_API_KEY = stringPreferencesKey("openai_api_key")
    }

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            mode = when (prefs[Keys.APP_MODE]) {
                "HOST" -> AppMode.HOST
                "CLIENT" -> AppMode.CLIENT
                else -> AppMode.HOST
            },
            hostIp = prefs[Keys.HOST_IP] ?: "",
            hostPort = prefs[Keys.HOST_PORT] ?: 8080
        )
    }

    val geminiApiKeyFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.GEMINI_API_KEY]
    }

    val openAiApiKeyFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.OPENAI_API_KEY]
    }

    suspend fun updateMode(mode: AppMode) {
        context.dataStore.edit { prefs ->
            prefs[Keys.APP_MODE] = mode.name
        }
    }

    suspend fun updateHostInfo(ip: String, port: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HOST_IP] = ip
            prefs[Keys.HOST_PORT] = port
        }
    }

    suspend fun updateGeminiApiKey(key: String?) {
        context.dataStore.edit { prefs ->
            if (key.isNullOrBlank()) {
                prefs.remove(Keys.GEMINI_API_KEY)
            } else {
                prefs[Keys.GEMINI_API_KEY] = key.trim()
            }
        }
    }

    suspend fun updateOpenAiApiKey(key: String?) {
        context.dataStore.edit { prefs ->
            if (key.isNullOrBlank()) {
                prefs.remove(Keys.OPENAI_API_KEY)
            } else {
                prefs[Keys.OPENAI_API_KEY] = key.trim()
            }
        }
    }
}
