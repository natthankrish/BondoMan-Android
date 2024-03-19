package com.example.bondoman.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
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
import com.example.bondoman.viewModels.TransactionsViewModel
import java.util.Calendar
import java.util.Date


class AddTransaction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaksi)
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
        }

    }

    companion object {
        const val TITLE = "TITLE"
        const val TYPE = "TYPE"
        const val AMOUNT = "AMOUNT"
        const val LOCATION = "LOCATION"
    }

}