package com.example.bondoman.retrofits

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
    fun getRetroClientInstance() : Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://pbd-backend-2024.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}