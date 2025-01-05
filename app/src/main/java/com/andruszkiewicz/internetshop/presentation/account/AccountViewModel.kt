package com.andruszkiewicz.internetshop.presentation.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: ProductRepository
): ViewModel() {

    private val TAG = AccountViewModel::class.java.simpleName

    private val _users = MutableStateFlow(emptyList<UserModel>())
    val users = _users.asStateFlow()

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _users.update {
                repository.getUsers()
            }
        }
    }
}