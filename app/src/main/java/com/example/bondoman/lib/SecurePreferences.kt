package com.example.bondoman.lib

import android.content.Context
import android.content.SharedPreferences
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

    fun clearToken(){
        sharedPreferences.edit().remove("token").apply()
    }
}