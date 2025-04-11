package com.sysinteg.pawlly.repository

import com.sysinteg.pawlly.api.AuthApi
import com.sysinteg.pawlly.model.AuthRequest
import com.sysinteg.pawlly.model.AuthResponse
import com.sysinteg.pawlly.model.User
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun signUp(user: User): Result<AuthResponse> {
        return try {
            val response = authApi.signUp(user)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = authApi.login(AuthRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(token: String): Result<User> {
        return try {
            val response = authApi.getCurrentUser(token)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 