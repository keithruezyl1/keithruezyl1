package com.sysinteg.pawlly.model

data class AuthResponse(
    val token: String?,
    val message: String,
    val success: Boolean
) 