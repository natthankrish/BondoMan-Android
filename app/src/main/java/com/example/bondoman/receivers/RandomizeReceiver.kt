package com.example.bondoman.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RandomizeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.bondoman.RANDOMIZE_TRANSACTION") {
            Log.d("AddTransaction", "Received")
        }
    }
}