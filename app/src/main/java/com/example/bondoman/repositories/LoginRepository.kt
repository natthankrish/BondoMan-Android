package com.example.bondoman.repositories

import android.util.Log
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.apiServices.LoginRequest
import com.example.bondoman.lib.SecurePreferences


class LoginRepository(private val authService: IAuthService, private val securePreferences : SecurePreferences) {
    suspend fun login(email: String, password: String) : Result<String>{
        val request = LoginRequest().apply {
            this.email = email.trim()
            this.password = password
        }
        return try {
            val response = authService.login(request)
            val token = response.token
            if(token != null){
                Log.e("Token", token)
                securePreferences.saveToken(token)
                securePreferences.saveEmail(email)
                Result.success(token)
            }else{
                Log.e("Token", "Token is null")
                Result.failure(Exception("Token is null"))
            }
        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
