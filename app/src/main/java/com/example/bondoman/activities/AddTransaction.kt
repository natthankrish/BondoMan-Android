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

    fun showDatePickerDialog(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val editTextDate : EditText = findViewById(R.id.editTextDate)

        val datePickerDialog = DatePickerDialog(this,
            { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                editTextDate.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            }, year, month, day)

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY)
    }
}