package com.andruszkiewicz.internetshop.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products = _products.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _products.update {
                productRepository.getProducts()
            }
        }
    }

    fun addProductToOrder(product: ProductModel, user: UserModel) {
        viewModelScope.launch {
            val product = productRepository.postOrderProduct(user.email, product.id, 1)

            Log.d(TAG, product.toString())

            if (product != null) {
                val oldOrderList = user.order

                val newListOfProducts =
                    if (oldOrderList != null) oldOrderList.products.toMutableList()
                    else mutableListOf()


                newListOfProducts.add(product)

                Log.d(TAG, "addProductToOrder: newListOfProducts: ${newListOfProducts}")

                val newUser = user.copy(
                    order = user.order?.copy(
                        products = newListOfProducts?.toList() ?: emptyList()
                    )
                )

                Log.d(TAG, "addProductToOrder: newUser: ${newUser}")

                GlobalUser.updateUser(newUser)
            }
        }
    }
}