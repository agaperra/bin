package com.agaperra.bin.utils.network
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData

class InternetReceiver : BroadcastReceiver() {

    companion object {
        private val state: MutableLiveData<Boolean> = MutableLiveData()
    }


    fun checkState(context: Context?): Boolean {
        context?.let {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } ?: return false
    }

    override fun onReceive(receiveContext: Context?, intent: Intent?) {
        when (checkState(receiveContext)) {
            true -> state.value = false
            false -> state.value = true
        }
    }

}