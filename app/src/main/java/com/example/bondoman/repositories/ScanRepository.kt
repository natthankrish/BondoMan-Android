package com.example.bondoman.repositories

import UploadResponse
import android.content.Context
import android.util.Log
import com.example.bondoman.apiServices.IScanService
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.retrofits.Retro
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanRepository(private val context : Context) {
    suspend fun uploadPhoto(file: File): UploadResponse {
        try {
            val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val token = SecurePreferences(context).getToken()
            Log.d("Token", "Bearer $token")
            val response = Retro().getRetroClientInstance().create(IScanService::class.java).scan("Bearer $token", body)
            Log.d("Response", response.toString())
            return response
        } catch (e: Exception) {
            Log.e("UploadPhoto", "Error uploading photo", e)
            throw e  // Re-throw if you want to handle it in the calling code
        }
    }

}