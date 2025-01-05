package com.andruszkiewicz.internetshop.network.dto

data class UserRequest(
    val email: String,
    val password: String,
    val status: String
)
