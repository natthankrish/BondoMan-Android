package com.example.bondoman.apiServices

import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Part

interface IScanService {
    @POST("/bill/upload")
    suspend fun scan(@Part file: MultipartBody.Part)
}