package com.andruszkiewicz.internetshop.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKey {
    val EMAIL = stringPreferencesKey("user_email")
    val PASSWORD = stringPreferencesKey("user_password")
}