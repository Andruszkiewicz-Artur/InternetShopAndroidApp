package com.andruszkiewicz.internetshop.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andruszkiewicz.internetshop.domain.model.QuantityModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.utils.GlobalUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: ProductRepository
): ViewModel() {

    fun changeStateOfProductCount(type: TypeOfButtonClickInOrder, quantityModel: QuantityModel) {
        val newQuantity = if (type == TypeOfButtonClickInOrder.Add) quantityModel.quantity + 1 else quantityModel.quantity - 1

        GlobalUser.user.value?.let { user ->

            if (newQuantity == 0) {
                viewModelScope.launch(Dispatchers.IO) {
                    if (repository.deleteProductFormOrder(quantityModel.id)) {
                        GlobalUser.updateUser(
                            user.copy(
                                order = user.order?.copy(
                                    products = user.order.products.mapNotNull {
                                        if (it.id == quantityModel.id) null
                                        else it
                                    }
                                )
                            )
                        )
                    }
                }
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = repository.postOrderProduct(user.email, quantityModel.product.id, newQuantity)

                    if (result != null) {
                        GlobalUser.updateUser(
                            user.copy(
                                order = user.order?.copy(
                                    products = user.order.products.map {
                                        if (it.id == result.id) result
                                        else it
                                    }
                                )
                            )
                        )
                    }
                }
            }

        }
    }
}