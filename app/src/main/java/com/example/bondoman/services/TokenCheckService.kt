package com.example.bondoman.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.bondoman.apiServices.IAuthService
import com.example.bondoman.retrofits.LoginRetro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TokenCheckService(private val token : String): Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            while (true){
                checkToken(token)
                delay(5000)
            }
        }
        return START_STICKY
    }

    private suspend fun checkToken(userToken : String){
        val tokenService = LoginRetro().getRetroClientInstance().create(IAuthService::class.java)
        try {
            val response = tokenService.checkToken("Bearer $userToken")
            if(response.nim != null){
                Log.d("Token", "Token is not expired")
            }else{
                Log.d("Expired", "Token is expired")
            }

        }catch (e : Exception){
            e.printStackTrace()
        }
    }


}