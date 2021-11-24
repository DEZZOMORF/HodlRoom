package com.lampa.financulator.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(@ApplicationContext context: Context?) {

    companion object {
        private const val SHARED_PREFERENCES_FILE_NAME = "financulator_pref"
        private val ACCESS_TOKEN = "access_token"
    }

    private val sharedPreferences: SharedPreferences? =
        context?.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    var userAccessToken: String?
        get() = sharedPreferences?.getString(ACCESS_TOKEN, null)
        set(token) {
            sharedPreferences?.edit {
                putString(ACCESS_TOKEN, token)
            }
        }
}