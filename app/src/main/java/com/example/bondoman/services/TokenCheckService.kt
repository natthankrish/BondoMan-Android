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

class TokenCheckService: Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        serviceScope.launch {
            while (true){
                val securePreferences = SecurePreferences(applicationContext)
                val token = securePreferences.getToken()
                val isValid = checkToken(token)
                if(!isValid){
                    sendTokenExpiredBroadcast()
                    securePreferences.clearToken()
                    stopSelf()
                    break
                }else{
                    delay(5000)
                }
            }
        }
        return START_STICKY
    }

    private suspend fun checkToken(userToken : String?) : Boolean{
        val tokenService = Retro().getRetroClientInstance().create(IAuthService::class.java)
        return try {
            val response = tokenService.checkToken("Bearer $userToken")
            if(response.nim == null){
                Log.d("Expired", "Token is expired")
                false
            }else{
                Log.d("Token", "Token is not expired")
                true
            }

        }catch (e : Exception){
            e.printStackTrace()
            return false
        }
    }

    private fun sendTokenExpiredBroadcast(){
        Intent().also { intent ->
            intent.action = "com.example.bondoman.TOKEN_EXPIRED"
            sendBroadcast(intent)
        }
    }

}