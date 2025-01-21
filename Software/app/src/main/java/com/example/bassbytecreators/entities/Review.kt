package com.example.bassbytecreators.entities

data class Review(
    val recenzija_id: Int,
    val rating: Int,
    val opis: String,
    val user_id: Int,
    val dj_id: Int,
    val reviewer: String? = null
)
