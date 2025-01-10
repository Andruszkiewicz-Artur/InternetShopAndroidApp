package com.andruszkiewicz.internetshop.utils

import android.util.Log
import com.andruszkiewicz.internetshop.domain.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object GlobalUser {

    private val TAG = GlobalUser::class.java.simpleName

    private val _user = MutableStateFlow<UserModel?>(null)
    val user = _user.asStateFlow()

    fun updateUser(user: UserModel?) {
        Log.d(TAG, user.toString())
        _user.update { user }
    }

}