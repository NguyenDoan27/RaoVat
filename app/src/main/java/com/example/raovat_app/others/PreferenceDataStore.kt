package com.example.raovat_app.others

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DataUser")

class PreferenceDataStore(private val context: Context) {


    private object PreferencesKeys {
        val TOKEN : Preferences.Key<String> = stringPreferencesKey("Token_User")

    }
    suspend fun saveToken(token :String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = token
        }

    }

    fun readToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.TOKEN]
        }
    }
//    suspend fun updateToken(newToken: String) {
//        context.dataStore.edit { preferences ->
//            preferences[PreferencesKeys.TOKEN] = newToken
//        }
//    }

    suspend fun deleteToken(){
        context.dataStore.edit { preference ->
            preference[PreferencesKeys.TOKEN] = ""
        }
    }
}