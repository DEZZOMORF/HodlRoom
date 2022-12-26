package com.dezzomorf.financulator.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectionManager @Inject constructor(
    @ApplicationContext private val context: Context
    ) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?

    var isConnected = MutableStateFlow(isAvailableConnection())

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    runBlocking {
                        isConnected.emit(true)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    runBlocking {
                        isConnected.emit(false)
                    }
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
                runBlocking {
                    isConnected.emit(isAvailableConnection())
                }
            }
        }
    }

    private fun isAvailableConnection(): Boolean {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true || capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }
}