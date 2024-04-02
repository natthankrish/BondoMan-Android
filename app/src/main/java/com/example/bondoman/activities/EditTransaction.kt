package com.example.bondoman.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.bondoman.R

class EditTransaction() : BaseActivity() {


    private lateinit var location : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaksi)
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
        location = findViewById(R.id.editTextLocation)
        val seeLocationText : TextView = findViewById(R.id.textViewSeeLocationLabel)
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
        seeLocationText.setOnClickListener {
            seeLocation()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun seeLocation(){
        val locationText = location.text.toString()
        val gmmIntentUri = Uri.parse("geo:0,0?q=$locationText")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

}