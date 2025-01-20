package com.example.bassbytecreators.entities

data class User(
    val user_id: Int,
    val username: String,
    val first_last_name: String,
    val email: String,
    val password: String? = null,
    val role: String
)
