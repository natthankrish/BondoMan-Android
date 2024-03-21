package com.example.bondoman.apiServices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("token")
    @Expose
    var token: String? = null
}