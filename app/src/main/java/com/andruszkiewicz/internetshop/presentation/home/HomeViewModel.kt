package com.andruszkiewicz.internetshop.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    companion object {
        private val TAG = "HomeViewModel_TAG"
    }

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products.asStateFlow()

    init {
        Log.d(TAG, "Start working vm")

        viewModelScope.launch {
            productRepository.getProducts().collectLatest { products ->
                _products.update { products }
                Log.d(
                    TAG,
                    products.toString()
                )
            }
        }
    }

    fun showLog() {
        Log.d(TAG, "Show Log")
    }
}