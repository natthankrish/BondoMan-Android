package com.example.bondoman.lib

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePreferences(context : Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences : SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "SharedPreferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token : String){
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun getToken() : String?{
        return sharedPreferences.getString("token", null)
    }

    fun clear(){
        sharedPreferences.edit().remove("token").apply()
        sharedPreferences.edit().remove("email").apply()
    }

    fun saveEmail(email : String){
        sharedPreferences.edit().putString("email", email).apply()
        val email = sharedPreferences.getString("email",null)
        if(email != null){
            Log.e("email :", email)
        }
    }

    fun getEmail() : String?{
        return sharedPreferences.getString("email", null)
    }
}