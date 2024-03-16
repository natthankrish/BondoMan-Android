package com.example.bondoman.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.bondoman.R
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.apiServices.LoginRequest
import com.example.bondoman.apiServices.LoginResponse
import com.example.bondoman.retrofits.LoginRetro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        var loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{
            login()
        }

    }

    fun login(){
        val request = LoginRequest()
        request.email = email.text.toString().trim()
        request.password = password.text.toString().trim()

        val retro = LoginRetro().getRetroClientInstance().create(IAuthService::class.java)
        retro.login(request).enqueue(object  : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val token = response.body()?.token
                if (token != null) {
                    Log.e("Token", token)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val errorMessage = t.message // Menyimpan nilai t.message ke dalam variabel lokal
                if(errorMessage != null){
                    Log.e("Error : ", errorMessage) // Menggunakan variabel lokal
                } else {
                    Log.e("Error", "On failure, no message available")
                }
            }

        })
    }
}