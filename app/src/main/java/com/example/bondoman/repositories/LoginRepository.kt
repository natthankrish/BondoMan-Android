package com.example.bondoman.repositories

import android.util.Log
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.apiServices.LoginRequest


class LoginRepository(private val authService: IAuthService) {
    suspend fun login(email: String, password: String) {
        val request = LoginRequest().apply {
            this.email = email.trim()
            this.password = password
        }
        try {
            val response = authService.login(request)
            val token = response.token
            if(token != null){
                Log.e("Token", token)
            }else{
                Log.e("Token", "Token is null")
            }
        } catch (e: Exception) {
            Log.e("Error : ", e.message ?: "Unknown error occurred")
        }
    }
}
