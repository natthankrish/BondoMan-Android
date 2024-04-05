package com.example.bondoman.retrofits

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
//    private val baseUrl = "https://pbd-backend-2024.vercel.app/api/"
    private val baseUrl = "http://10.0.2.2:3000/api/"
    fun getRetroClientInstance() : Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}