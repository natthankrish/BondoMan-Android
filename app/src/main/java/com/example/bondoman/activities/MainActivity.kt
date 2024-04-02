package com.example.bondoman.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.bondoman.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {
    private lateinit var randomizeReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        randomizeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.e("Randomize", "Broadcast received")
                if (intent?.action == "com.example.bondoman.RANDOMIZE_TRANSACTION") {
                    val randomizeIntent = Intent(this@MainActivity, AddTransaction::class.java)
                    randomizeIntent.putExtras(intent)
                    startActivityForResult(randomizeIntent,1)
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
        val randomizeIntentFilter = IntentFilter("com.example.bondoman.RANDOMIZE_TRANSACTION")
        registerReceiver(randomizeReceiver, randomizeIntentFilter, RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}