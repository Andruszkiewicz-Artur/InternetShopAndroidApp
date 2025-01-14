package com.andruszkiewicz.internetshop.domain.repository

import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import com.andruszkiewicz.internetshop.domain.model.OrderModel
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.QuantityModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.network.dto.OrderDto
import com.andruszkiewicz.internetshop.network.dto.ProductDto
import com.andruszkiewicz.internetshop.network.dto.QuantityDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProductRepository {

    suspend fun createProduct(product: ProductModel)

    suspend fun getProducts(): List<ProductModel>

    suspend fun getUsers(): List<UserModel>

    suspend fun postOrderProduct(
            email: String,
            productId: Long,
            quantity: Int
        ): QuantityModel?

    suspend fun deleteProductFormOrder(idOrderProduct: Long): Boolean

    suspend fun createUser(
        email: String,
        password: String,
        status: UserStatus
    ): UserModel?

    suspend fun createEditProduct(
        product: ProductModel
    ): ProductModel?

    suspend fun logInUser(
        email: String,
        password: String
    ): UserModel?

    suspend fun removeProduct(
        productId: Long
    ): ProductModel?

    suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): UserModel?

    suspend fun buyOrder(id: Long): Boolean

    suspend fun getOrders(email: String): List<OrderModel>
}