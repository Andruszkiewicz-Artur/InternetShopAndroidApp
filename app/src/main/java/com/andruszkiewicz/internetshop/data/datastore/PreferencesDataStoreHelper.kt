package com.andruszkiewicz.internetshop.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

object PreferencesDataStoreHelper {

    private val TAG = PreferencesDataStoreHelper::class.java.simpleName

    private const val PREFERENCES_DATA_STORE = "preferences_data_store"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_DATA_STORE)

    private suspend fun saveValueToDataStore(
        value: String,
        key: Preferences.Key<String>,
        context: Context
    ) {
        Log.d(TAG, "saveValueToDataStore: value: $value")

        context.dataStore
            .edit { saveData ->
                saveData[key] = value
            }
    }

    private fun getValueFromDataStore(
        key: Preferences.Key<String>,
        context: Context
    ): Flow<String?> {
        Log.d(TAG, "getValueFromDataStore: key: $key")

        return context.dataStore.data
            .catch { exception ->
                when (exception) {
                    is IOException -> emit(emptyPreferences())
                    else -> throw exception
                }
            }.map { readData ->
                readData[key]
            }
    }

    private suspend fun removeValueFromDataStore(
        key: Preferences.Key<String>,
        context: Context
    ) = context
        .dataStore
        .edit {
            it.remove(key)
        }

    internal suspend fun removeDataFromDataStore(
        keys: List<Preferences.Key<String>>,
        context: Context
    ) = keys.forEach { key ->
        removeValueFromDataStore(key, context)
    }

    object User {
        internal suspend fun getEmailAndPassword(
            context: Context
        ): Pair<String?, String?> {
            val email = getValueFromDataStore(PreferencesKey.EMAIL, context).firstOrNull()
            val password = getValueFromDataStore(PreferencesKey.PASSWORD, context).firstOrNull()
            return Pair(email, password)
        }

        internal suspend fun saveEmailAndPassword(
            email: String,
            password: String,
            context: Context
        ) {
            saveValueToDataStore(
                email,
                PreferencesKey.EMAIL,
                context
            )

            saveValueToDataStore(
                password,
                PreferencesKey.PASSWORD,
                context
            )
        }
    }

}