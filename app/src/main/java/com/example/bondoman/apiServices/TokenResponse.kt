package com.example.bondoman.apiServices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TokenResponse {
    @SerializedName("nim")
    @Expose
    var nim : String? = null

    @SerializedName("iat")
    @Expose
    var iat : String? = null

    @SerializedName("exp")
    @Expose
    var exp : String? = null
}