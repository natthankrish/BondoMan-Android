package com.example.bondoman

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.junit.Test
import java.io.File
import org.junit.Assert.assertEquals

class UploadTest {
    @Test
    fun testUploadSuccess(){
        val filename = "test1.png" // or "test1.jpg" for a JPEG file
        val mediaType = when (filename.substringAfterLast('.')) {
            "jpg", "jpeg" -> "image/jpeg".toMediaType()
            "png" -> "image/png".toMediaType()
            else -> throw IllegalArgumentException("Unsupported file type")
        }
        val fileURL = this::class.java.classLoader?.getResource("test1.png")
        if(fileURL?.toURI() != null){
            val file = File(fileURL.toURI())
            val fileBody = file.asRequestBody(mediaType)
            val filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(filePart)
                .build()
            val request = Request.Builder()
                .url("https://pbd-backend-2024.vercel.app/api/bill/upload")
                .post(requestBody)
                .build()
            val client = OkHttpClient()
            client.newCall(request).execute().use { response ->
                println("Masuk ke sini")
                assertEquals(200, response.code)
            }

        }
    }
}