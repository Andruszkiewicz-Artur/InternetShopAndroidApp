package com.andruszkiewicz.internetshop.domain.model

import android.os.Parcelable
import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import kotlinx.parcelize.Parcelize

data class UserModel(
    val email: String,
    val status: UserStatus,
    val order: OrderModel?
) {

    fun toUserEmailAndStatus() = UserEmailAndStatusModel(
        email = email,
        status = status
    )

}
