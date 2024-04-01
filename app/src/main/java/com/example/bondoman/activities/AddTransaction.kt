package com.example.bondoman.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R
import android.widget.Spinner


class AddTransaction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaksi)
        supportActionBar?.title = "Add Transaction"

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val spinnerCategory : Spinner = findViewById(R.id.spinnerCategory)
        val editTextAmount = findViewById<EditText>(R.id.editTextAmount)
        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type_options,
            R.layout.spinner_dropdown
        )

        editTextTitle.setText(intent.getStringExtra("TITLE"))
        spinnerCategory.setSelection(intent.getIntExtra("TYPE", 0))
        editTextAmount.setText(intent.getFloatExtra("AMOUNT", 0f).toString())

        spinnerCategory.adapter = adapter

        val submitButton : Button = findViewById(R.id.buttonSubmit)
        submitButton.setOnClickListener {
            val title = editTextTitle.text.toString()
            val category = spinnerCategory.selectedItem.toString()
            val amount = editTextAmount.text.toString().toFloatOrNull() ?: 0f
            val location = editTextLocation.text.toString()

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