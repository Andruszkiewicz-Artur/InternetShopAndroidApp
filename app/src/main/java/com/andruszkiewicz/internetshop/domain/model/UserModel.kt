package com.andruszkiewicz.internetshop.domain.model

data class UserModel(
    val email: String,
    val order: OrderModel?
)
