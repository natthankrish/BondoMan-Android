package com.example.bondoman.activities

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bondoman.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale


class AddTransaction : BaseActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var editTextLocation : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaksi)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val spinnerCategory : Spinner = findViewById(R.id.spinnerCategory)
        val editTextAmount = findViewById<EditText>(R.id.editTextAmount)
        editTextLocation = findViewById(R.id.editTextLocation)


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
            if (editTextTitle.text.toString() != "") {
                val title = editTextTitle.text.toString()
                val category = spinnerCategory.selectedItem.toString()
                val amount = editTextAmount.text.toString().toFloatOrNull() ?: 0f
                val location = editTextLocation.text.toString()

                if (amount > 0f) {
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
                } else {
                    Toast.makeText(this,
                        "Price should be greater than 0",
                        Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this,
                    "Name cannot be empty",
                    Toast.LENGTH_SHORT).show()
            }
        }
        getLastLocation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    updateLocationEditTextWithPlaceName(location.latitude, location.longitude)
                }
            }
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }
    private fun updateLocationEditTextWithPlaceName(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                editTextLocation.setText(address.getAddressLine(0).toString())
            } else {
                editTextLocation.setText("${latitude}, ${longitude}")
            }
        } catch (e: IOException) {
            Log.e("Location", "Service Not Available", e)
        }
    }
    companion object {
        const val TITLE = "TITLE"
        const val TYPE = "TYPE"
        const val AMOUNT = "AMOUNT"
        const val LOCATION = "LOCATION"
        const val REQUEST_LOCATION_PERMISSION = 1 // Define the constant here

    }
}