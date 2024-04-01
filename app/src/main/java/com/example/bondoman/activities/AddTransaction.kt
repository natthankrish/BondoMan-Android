package com.example.bondoman.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R
import android.widget.Spinner
import com.example.bondoman.adapter.RecyclerViewAdapter
import com.example.bondoman.entities.Transaction
import com.example.bondoman.services.TokenCheckService
import com.example.bondoman.viewModels.TransactionsViewModel
import java.util.Calendar
import java.util.Date


class AddTransaction : AppCompatActivity() {
    private lateinit var tokenExpiredReceiver: BroadcastReceiver
    private lateinit var tokenServiceIntent : Intent
    private var isReceiverRegistered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaksi)
        tokenServiceIntent= Intent(this, TokenCheckService::class.java)
        startService(tokenServiceIntent)

        tokenExpiredReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null && intent.action != null){
                    Log.e("Receive", intent.action.toString())
                }
                if(intent?.action == "com.example.bondoman.TOKEN_EXPIRED"){
                    val loginIntent = Intent(this@AddTransaction, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    stopService(tokenServiceIntent)
                    startActivity(loginIntent)
                    finish()
                }
            }

        }
        supportActionBar?.title = "Add Transaction"

        val spinnerCategory : Spinner = findViewById(R.id.spinnerCategory)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type_options,
            R.layout.spinner_dropdown
        )

        spinnerCategory.adapter = adapter

        val submitButton : Button = findViewById(R.id.buttonSubmit)
        submitButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.editTextTitle).text.toString()
            val category = spinnerCategory.selectedItem.toString()
            val amount = findViewById<EditText>(R.id.editTextAmount).text.toString().toFloatOrNull() ?: 0f
            val location = findViewById<EditText>(R.id.editTextLocation).text.toString()

            val replyIntent = Intent()
            if (title.isEmpty()) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(TITLE, title)
                replyIntent.putExtra(TYPE, category)
                replyIntent.putExtra(AMOUNT, amount)
                replyIntent.putExtra(LOCATION, location)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
            overridePendingTransition(0, R.anim.slide_down)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(0, R.anim.slide_down)
    }

    companion object {
        const val TITLE = "TITLE"
        const val TYPE = "TYPE"
        const val AMOUNT = "AMOUNT"
        const val LOCATION = "LOCATION"
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