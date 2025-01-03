package com.andruszkiewicz.internetshop.domain.model

import com.andruszkiewicz.internetshop.domain.enums.UserStatus

data class UserModel(
    val email: String,
    val password: String,
    val status: UserStatus,
    val orders: List<OrderModel>
)
