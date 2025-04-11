package com.sysinteg.pawlly.model

data class User(
    val userId: Long? = null,
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val address: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null,
    val profilePicture: ByteArray? = null,
    val createdAt: String? = null
) 