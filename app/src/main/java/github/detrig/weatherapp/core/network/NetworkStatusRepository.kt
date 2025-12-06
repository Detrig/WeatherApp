package github.detrig.weatherapp.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkStatusRepository {
    fun observeNetworkStatus(): Flow<NetworkStatus>
}