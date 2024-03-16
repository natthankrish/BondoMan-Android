package com.example.bondoman.repositories

import android.util.Log
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.apiServices.LoginRequest
import com.example.bondoman.lib.SecurePreferences


class LoginRepository(private val authService: IAuthService, private val securePreferences : SecurePreferences) {
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
                securePreferences.saveToken(token)
                val savedToken = securePreferences.getToken()
                if(savedToken != null){
                    Log.d("SaveToken : ", savedToken)
                }else{
                    Log.e("SavedToken", "Token is null")
                }
            }else{
                Log.e("Token", "Token is null")
            }
        } catch (e: Exception) {
            Log.e("Error : ", e.message ?: "Unknown error occurred")
        }
    }
}
