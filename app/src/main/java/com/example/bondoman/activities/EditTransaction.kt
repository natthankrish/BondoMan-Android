package com.example.bondoman.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R
import android.widget.Spinner
import android.widget.TextView
import java.util.Calendar


class EditTransaction() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaksi)
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
}