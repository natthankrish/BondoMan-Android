package com.example.bondoman
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class LoginTest {

    @Test
    fun testLoginSuccess() {

        val jsonMediaType = "application/json; charset=utf-8".toMediaType()

        val requestBody = """
            {
                "email": "13521139@std.stei.itb.ac.id",
                "password": "password_13521139"
            }
            """.trimIndent().toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("https://pbd-backend-2024.vercel.app/api/auth/login")
            .post(requestBody)
            .build()
        val client = OkHttpClient()
        client.newCall(request).execute().use { response ->
            // Assert the response code to verify successful login
            // You might want to adjust the expected response code based on your API's specification
            assertEquals(200, response.code)

            // Optionally, you can assert the response body or specific parts of it
            // For example, you might want to check if a token is present in the response
            val responseBody = response.body?.string() ?: ""
            assert(responseBody.contains("token")) // Adjust this based on your actual response structure
        }

    }

    @Test
    fun testLoginFail(){
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
            {
                "email": "wrong email",
                "password": "wrong password"
            }
            """.trimIndent().toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url("https://pbd-backend-2024.vercel.app/api/auth/login")
            .post(requestBody)
            .build()
        val client = OkHttpClient()
        client.newCall(request).execute().use { response ->
            // Assert the response code to verify successful login
            // You might want to adjust the expected response code based on your API's specification
            assertEquals(400, response.code)

            // Optionally, you can assert the response body or specific parts of it
            // For example, you might want to check if a token is present in the response
            val responseBody = response.body?.string() ?: ""
            assertTrue("Expected 'Invalid email' in the response body", responseBody.contains("Invalid email"))

        }

    }

}