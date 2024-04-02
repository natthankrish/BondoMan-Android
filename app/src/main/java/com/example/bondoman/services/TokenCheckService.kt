package com.example.bondoman.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.retrofits.Retro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class TokenValidationResult {
    object Valid : TokenValidationResult()
    object Invalid : TokenValidationResult()
    object ConnectionError : TokenValidationResult()
}

class TokenCheckService: Service() {
    companion object {
        @Volatile
        var isOperationRunning = false
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(!isOperationRunning){
            isOperationRunning = true
            serviceScope.launch {
                while (true) {
                    val securePreferences = SecurePreferences(applicationContext)
                    val token = securePreferences.getToken()
                    when (checkToken(token)) {
                        TokenValidationResult.Valid -> {
                            Log.d("Token", "Token still valid")
                            delay(10000)
                        }
                        TokenValidationResult.Invalid -> {
                            Log.d("Token", "Token not valid")
                            sendTokenExpiredBroadcast()
                            securePreferences.clear()
                            stopSelf()
                            break
                        }
                        TokenValidationResult.ConnectionError -> {
                            Log.d("Token", "Connectiob error")
                            delay(30000)
                        }
                    }
                }
            }
        }

        return START_STICKY
    }


    private suspend fun checkToken(userToken: String?): TokenValidationResult {
        val tokenService = Retro().getRetroClientInstance().create(IAuthService::class.java)
        return try {
            val response = tokenService.checkToken("Bearer $userToken")
            if (response.nim == null) {
                TokenValidationResult.Invalid
            } else {
                TokenValidationResult.Valid
            }
        } catch (e: HttpException) {
            if(e.code() == 401){
                TokenValidationResult.Invalid
            }else{
                TokenValidationResult.ConnectionError
            }
        }catch (e : Exception){
            TokenValidationResult.ConnectionError
        }
    }


    private fun sendTokenExpiredBroadcast(){
        Intent().also { intent ->
            intent.action = "com.example.bondoman.TOKEN_EXPIRED"
            sendBroadcast(intent)
        }
    }

}