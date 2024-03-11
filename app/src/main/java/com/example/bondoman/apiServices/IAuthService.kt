package com.example.bondoman.apiServices

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IAuthService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest)
    @POST("/auth/token")
    suspend fun checkToken(@Header("Authorization") token: String)
}