package com.example.bondoman.apiServices

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IAuthService {
    @POST("auth/login/")
    fun login(@Body request: LoginRequest) : Call<LoginResponse>
    @POST("auth/token")
    suspend fun checkToken(@Header("Authorization") token: String)
}