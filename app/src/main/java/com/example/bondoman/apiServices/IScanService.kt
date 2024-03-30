package com.example.bondoman.apiServices

import UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part

interface IScanService {
    @POST("/bill/upload")
    suspend fun scan(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ) : UploadResponse
}