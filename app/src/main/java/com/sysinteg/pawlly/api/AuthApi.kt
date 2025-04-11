package com.sysinteg.pawlly.api

import com.sysinteg.pawlly.model.AuthRequest
import com.sysinteg.pawlly.model.AuthResponse
import com.sysinteg.pawlly.model.User
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("api/auth/signup")
    suspend fun signUp(@Body user: User): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @GET("api/auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>
} 