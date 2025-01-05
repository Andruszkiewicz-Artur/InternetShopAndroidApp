package com.andruszkiewicz.internetshop.domain.repository

import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.QuantityModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
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
        isAdmin: Boolean
    ): Boolean

    suspend fun createProduct(
        name: String,
        prize: Float
    ): Boolean
}