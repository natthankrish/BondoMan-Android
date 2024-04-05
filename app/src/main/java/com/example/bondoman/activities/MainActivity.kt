package com.example.bondoman.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.bondoman.R
import com.example.bondoman.database.TransactionDatabase
import com.example.bondoman.entities.Transaction
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.repositories.TransactionRepository
import com.example.bondoman.services.TokenCheckService
import com.example.bondoman.viewModels.TransactionViewModelFactory
import com.example.bondoman.viewModels.TransactionsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.Date

class MainActivity : BaseActivity() {
    private lateinit var randomizeReceiver: BroadcastReceiver
    private var isReceiverRegistered = false
    private lateinit var bottomNavigationView: BottomNavigationView;
    private lateinit var navigationView: NavigationView;
    private lateinit var fragment: NavHostFragment;
    private lateinit var securePreferences: SecurePreferences
    private val newTransactionRequestCode = 1
    private val editTransactionRequestCode = 2
    private var isAddTransactionActivityRunning = false
    private val wordViewModel: TransactionsViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                TransactionDatabase.getInstance(this, CoroutineScope(
                    SupervisorJob()
                )
                ).transactionDao(), securePreferences)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        securePreferences = SecurePreferences(this)

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
                if (intent?.action == "com.example.bondoman.RANDOMIZE_TRANSACTION" && !isAddTransactionActivityRunning) {
                    isAddTransactionActivityRunning = true
                    val randomizeIntent = Intent(this@MainActivity, AddTransaction::class.java)
                    randomizeIntent.putExtras(intent)
                    startActivityForResult(randomizeIntent, newTransactionRequestCode)
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
        val randomizeIntentFilter = IntentFilter("com.example.bondoman.RANDOMIZE_TRANSACTION")
        registerReceiver(randomizeReceiver, randomizeIntentFilter, RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newTransactionRequestCode && resultCode == Activity.RESULT_OK) {
            val title = intentData?.getStringExtra(AddTransaction.TITLE) ?: ""
            val amount = intentData?.getFloatExtra(AddTransaction.AMOUNT, 0.0f) ?: 0.0f
            val type = intentData?.getStringExtra(AddTransaction.TYPE) ?: ""
            val location = intentData?.getStringExtra(AddTransaction.LOCATION) ?: ""

            val transaction = Transaction(
                id = 0,
                title = title,
                category = type,
                amount = amount,
                location = location,
                date = Date(),
                userEmail = securePreferences.getEmail() ?: ""
            )
            wordViewModel.insert(transaction)
            isAddTransactionActivityRunning = false
        }
    }
}