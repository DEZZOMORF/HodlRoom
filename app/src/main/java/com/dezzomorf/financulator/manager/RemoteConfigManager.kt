package com.dezzomorf.financulator.manager

import android.util.Log
import com.dezzomorf.financulator.extensions.toMap
import com.dezzomorf.financulator.util.RequestState
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor() {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        val configSettings = FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(0).build()
        setConfigSettingsAsync(configSettings)
        fetchAndActivate()
    }

    fun getDonationWallets(): RequestState<Map<String, *>> {
        val key = "donation_wallets"

        return try {
            val jsonString = remoteConfig.getString(key)
            val map = JSONObject(jsonString).toMap()
            if (map.isNotEmpty()) {
                RequestState.Success(map)
            } else {
                RequestState.GeneralError(Exception("Empty remote config map"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
            FirebaseCrashlytics.getInstance().recordException(Throwable(e.message))
            RequestState.GeneralError(e)
        }
    }
}