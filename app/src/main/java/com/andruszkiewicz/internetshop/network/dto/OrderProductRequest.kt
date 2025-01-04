package com.andruszkiewicz.internetshop.network.dto

data class OrderProductRequest(
    val email: String,
    val productId: Long,
    val quantity: Int
)
