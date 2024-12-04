package com.kolown.common.component

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

object NetworkStateManager {
    fun checkNetworkState(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val actNetwork: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        val cellularState = actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        val wifiState = actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        return cellularState || wifiState
    }
}
