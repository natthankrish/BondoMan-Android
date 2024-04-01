package com.example.bondoman.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.bondoman.R
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.repositories.AuthRepository
import com.example.bondoman.retrofits.Retro
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var loginButton: Button
    private lateinit var loginRepository: AuthRepository
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val securePreferences = SecurePreferences(this)
        loginRepository = AuthRepository(securePreferences)
        if(securePreferences.getToken() != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{
            lifecycleScope.launch {
                login(email.text.toString(), password.text.toString())
            }
        }
    }

    private suspend fun login(email : String, password : String){
        showLoadingDialog()
        val result = loginRepository.login(email, password)
        hideLoadingDialog()
        if(result.isSuccess){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val message = result.exceptionOrNull()?.message ?: "Login failed!"
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
        }
    }
    private fun showLoadingDialog() {
        loadingDialog = Dialog(this).apply {
            setContentView(R.layout.loading)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }
}