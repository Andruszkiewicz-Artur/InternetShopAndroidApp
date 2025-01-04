package com.andruszkiewicz.internetshop.network.dto

import com.andruszkiewicz.internetshop.domain.model.UserModel

data class UserDto(
    val email: String,
    val order: OrderDto?
) {

    fun toDomain() = UserModel(
        email = email,
        order = order?.toDomain()
    )

}

