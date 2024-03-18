package com.example.bondoman.activities

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R
import android.widget.Spinner
import java.util.Calendar


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
    }

}