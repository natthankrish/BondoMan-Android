package com.example.bondoman.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.repositories.LoginRepository
import com.example.bondoman.retrofits.Retro
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var loginButton: Button
    private lateinit var loginRepository: LoginRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val authService = Retro().getRetroClientInstance().create(IAuthService::class.java)
        val securePreferences = SecurePreferences(this)

        loginRepository = LoginRepository(authService, securePreferences)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{
            lifecycleScope.launch {
                val result = loginRepository.login(email.text.toString(), password.text.toString())
                if(result.isSuccess){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    val message = result.exceptionOrNull()?.message ?: "Login failed!"
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}