package com.example.bondoman.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.bondoman.R
import com.example.bondoman.services.TokenCheckService
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var tokenExpiredReceiver: BroadcastReceiver
    private lateinit var tokenServiceIntent : Intent
    private var isReceiverRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tokenServiceIntent= Intent(this, TokenCheckService::class.java)
        startService(tokenServiceIntent)

        tokenExpiredReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null && intent.action != null){
                    Log.e("Receive", intent.action.toString())
                }
                if(intent?.action == "com.example.bondoman.TOKEN_EXPIRED"){
                    val loginIntent = Intent(this@MainActivity, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    stopService(tokenServiceIntent)
                    startActivity(loginIntent)
                    finish()
                }
            }

        }
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            supportActionBar?.title = menuItem.title
            menuItem.onNavDestinationSelected(navController)
            true
        }
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
        if(isReceiverRegistered){
            unregisterReceiver(tokenExpiredReceiver)
            stopService(tokenServiceIntent)
            isReceiverRegistered = false
        }
    }
}