package com.example.bondoman.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.bondoman.R
import com.example.bondoman.services.TokenCheckService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var tokenExpiredReceiver: BroadcastReceiver
    private lateinit var randomizeReceiver: BroadcastReceiver
    private lateinit var tokenServiceIntent : Intent
    private var isReceiverRegistered = false
    private lateinit var bottomNavigationView: BottomNavigationView;
    private lateinit var navigationView: NavigationView;
    private lateinit var fragment: NavHostFragment;

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

        randomizeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "com.example.bondoman.RANDOMIZE_TRANSACTION") {
                    val randomizeIntent = Intent(this@MainActivity, AddTransaction::class.java)
                    randomizeIntent.putExtras(intent)
                    startActivity(randomizeIntent)
                }
            }
        }
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment)
        navigationView = findViewById<NavigationView>(R.id.navigation_view)
        fragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        val navFragmentLayoutParams = fragment.view?.layoutParams as ConstraintLayout.LayoutParams

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Change layout to show left navigation
            bottomNavigationView.visibility = View.GONE
            navigationView.visibility = View.VISIBLE
        } else {
            // Set up bottom navigation bar
            bottomNavigationView.visibility = View.VISIBLE
            navigationView.visibility = View.GONE
        }

        navigationView.setNavigationItemSelectedListener { menuItem : MenuItem ->
            supportActionBar?.title = menuItem.title
            menuItem.onNavDestinationSelected(navController)
            true
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            supportActionBar?.title = menuItem.title
            menuItem.onNavDestinationSelected(navController)
            true
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation

        val navFragmentLayoutParams = fragment.view?.layoutParams as ConstraintLayout.LayoutParams
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Change layout to show left navigation
            bottomNavigationView.visibility = View.GONE
            navigationView.visibility = View.VISIBLE
        } else {
            // Change layout to show bottom navigation bar
            bottomNavigationView.visibility = View.VISIBLE
            navigationView.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        val tokenIntentFilter = IntentFilter("com.example.bondoman.TOKEN_EXPIRED")
        registerReceiver(tokenExpiredReceiver, tokenIntentFilter)
        val randomizeIntentFilter = IntentFilter("com.example.bondoman.RANDOMIZE_TRANSACTION")
        registerReceiver(randomizeReceiver, randomizeIntentFilter)
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