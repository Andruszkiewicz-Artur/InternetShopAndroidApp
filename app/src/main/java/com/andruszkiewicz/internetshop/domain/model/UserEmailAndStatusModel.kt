package com.andruszkiewicz.internetshop.domain.model

import android.os.Parcelable
import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEmailAndStatusModel(
    val email: String,
    val status: UserStatus
): Parcelable
