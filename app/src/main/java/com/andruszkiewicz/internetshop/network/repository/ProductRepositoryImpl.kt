package com.andruszkiewicz.internetshop.network.repository

import android.util.Log
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.QuantityModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.network.dto.OrderProductRequest
import com.andruszkiewicz.internetshop.network.dto.QuantityDto
import com.andruszkiewicz.internetshop.network.dto.UserRequest
import com.andruszkiewicz.internetshop.network.service.ProductService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val service: ProductService
): ProductRepository {

    private val TAG = ProductRepositoryImpl::class.java.simpleName

    override suspend fun createProduct(product: ProductModel) {  }

    override suspend fun getProducts(): List<ProductModel> {
        val response = service.getProducts()

        Log.d(TAG, response.toString())
        Log.d(TAG, response.body().toString())

        val body = response.body()?.map {
            it.toDomain()
        }

        return body ?: emptyList()
    }

    override suspend fun getUsers(): List<UserModel> {
        val response = service.getUsers()

        Log.d(TAG, response.toString())
        Log.d(TAG, response.body().toString())

        val body = response.body()?.map {
            it.toDomain()
        }

        return body ?: emptyList()
    }

    override suspend fun postOrderProduct(
        email: String,
        productId: Long,
        quantity: Int
    ): QuantityModel? {
        val response = service.postProduct(
            OrderProductRequest(
                email = email,
                productId = productId,
                quantity = quantity
            )
        )
        val body = response.body()?.toDomain()

        Log.d(TAG, body.toString())

        return body
    }

    override suspend fun deleteProductFormOrder(idOrderProduct: Long): Boolean =
        service
            .deleteProduct(idOrderProduct)
            .isSuccessful

    override suspend fun createUser(email: String, password: String, isAdmin: Boolean): Boolean =
        service.postUser(
            UserRequest(
                email,
                password,
                if (isAdmin) "Admin" else "User"
            )
        ).isSuccessful

}