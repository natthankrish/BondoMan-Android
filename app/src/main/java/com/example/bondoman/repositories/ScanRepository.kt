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
    suspend fun uploadPhoto(file : File) : UploadResponse{
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val scanService = Retro().getRetroClientInstance().create(IScanService::class.java)

        val body = MultipartBody.Part.createFormData("source", file.name, requestFile)
        val securePreferences = SecurePreferences(context)
        val token = securePreferences.getToken()
        val response = scanService.scan("Bearer $token", body)
        Log.d("Response :", response.toString())
        return response;
    }
}