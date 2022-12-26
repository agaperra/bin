package com.agaperra.bin.utils.network

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}