package com.example.bondoman.activities

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.interfaces.ConnectivirtyObserver
import com.example.bondoman.observer.NetworkConnectivityObserver
import com.example.bondoman.services.TokenCheckService
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {
    protected lateinit var tokenServiceIntent: Intent
    protected lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private var isReceiverRegistered = false
    protected lateinit var tokenExpiredReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BaseActivity ", "On create")
        tokenServiceIntent = Intent(this, TokenCheckService::class.java)
        startService(tokenServiceIntent)
        Log.d("BaseActivity :", "Service Started")
        networkConnectivityObserver = NetworkConnectivityObserver(this)
        observeNetworkConnectivity()
    }

    protected open fun observeNetworkConnectivity() {
        lifecycleScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                if (status === ConnectivirtyObserver.Status.Unavailable || status === ConnectivirtyObserver.Status.Lost) {
                    showAlert()
                }
            }
        }
    }

    protected fun showAlert() {
        AlertDialog.Builder(this).apply {
            setTitle("Connection lost")
            setMessage("You aren't connected to internet right now")
            setPositiveButton("OK") { _, _ -> }
        }.show()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("com.example.bondoman.TOKEN_EXPIRED")
        registerReceiver(tokenExpiredReceiver, filter)
        isReceiverRegistered = true
    }

    override fun onStop() {
        super.onStop()
        if (isReceiverRegistered) {
            unregisterReceiver(tokenExpiredReceiver)
            isReceiverRegistered = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isReceiverRegistered) {
            unregisterReceiver(tokenExpiredReceiver)
            stopService(tokenServiceIntent)
            isReceiverRegistered = false
        }
    }
}
