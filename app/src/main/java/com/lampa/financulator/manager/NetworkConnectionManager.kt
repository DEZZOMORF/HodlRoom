package com.lampa.financulator.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectionManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        const val MESSAGE = "No internet connection"
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?

    var isConnected = MutableLiveData(isAvailableConnection())

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    isConnected.postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    isConnected.postValue(false)
                }

            })
        }
    }

    @Suppress("DEPRECATION")
    inner class PreNougatConnectionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                && intent?.action == "android.net.conn.CONNECTIVITY_CHANGE"
            ) { // pre nougat versions only!
                isConnected.postValue(isAvailableConnection())
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun isAvailableConnection(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork
            val capabilities = connectivityManager?.getNetworkCapabilities(network)
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
                    || capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        } else {
            connectivityManager?.activeNetworkInfo?.isConnected ?: false
        }
    }
}