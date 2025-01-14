package com.andruszkiewicz.internetshop.network.dto

import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import com.andruszkiewicz.internetshop.domain.model.UserEmailAndStatusModel
import com.andruszkiewicz.internetshop.domain.model.UserModel

data class UserDto(
    val email: String,
    val status: String,
    val order: OrderDto?
) {

    fun toDomain() = UserModel(
        email = email,
        order = order?.toDomain(),
        status = UserStatus.valueOf(status)
    )

    fun toUserEmailAndStatus() = UserEmailAndStatusModel(
        email = email,
        status = UserStatus.valueOf(status)
    )
}

