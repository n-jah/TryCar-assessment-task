package com.example.trycar_assessment_task.Network

import kotlinx.coroutines.flow.Flow

interface INetworkConnectivityObserver {
    fun isNetworkAvailable(): Boolean
    fun observeNetworkConnectivity(): Flow<Boolean>
}
