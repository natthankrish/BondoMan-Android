package com.example.bondoman.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R
import com.example.bondoman.services.TokenCheckService


class EditTransaction() : AppCompatActivity() {
    private lateinit var tokenExpiredReceiver: BroadcastReceiver
    private lateinit var tokenServiceIntent : Intent
    private var isReceiverRegistered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaksi)
        tokenServiceIntent= Intent(this, TokenCheckService::class.java)
        startService(tokenServiceIntent)

        tokenExpiredReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null && intent.action != null){
                    Log.e("Receive", intent.action.toString())
                }
                if(intent?.action == "com.example.bondoman.TOKEN_EXPIRED"){
                    val loginIntent = Intent(this@EditTransaction, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    stopService(tokenServiceIntent)
                    startActivity(loginIntent)
                    finish()
                }
            }

        }
        supportActionBar?.title = "Edit Transaction"

        val date : TextView = findViewById(R.id.editTextDate)
        val type : TextView = findViewById(R.id.editTextType)
        val title : EditText = findViewById(R.id.editTextTitle)
        val amount : EditText = findViewById(R.id.editTextAmount)
        val location : EditText = findViewById(R.id.editTextLocation)

        val intentData = intent

        title.setText(intentData?.getStringExtra("title") ?: "")
        amount.setText(intentData?.getFloatExtra("amount", 0.0f).toString())
        type.text = intentData?.getStringExtra("type") ?: ""
        location.setText(intentData?.getStringExtra("location") ?: "")
        date.text = intentData?.getStringExtra("date").toString()

        val deleteButton : Button = findViewById(R.id.buttonDelete)
        deleteButton.setOnClickListener{
            val replyIntent = Intent()
            replyIntent.putExtra("command", "delete")
            if (false) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra("id", intentData?.getStringExtra("id"))
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val submitButton : Button = findViewById(R.id.buttonSubmit)
        submitButton.setOnClickListener{
            val replyIntent = Intent()
            replyIntent.putExtra("command", "update")
            if (false) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra("id", intentData?.getStringExtra("id"))
                replyIntent.putExtra("title", title.text.toString())
                replyIntent.putExtra("amount", amount.text.toString())
                replyIntent.putExtra("location", location.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
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