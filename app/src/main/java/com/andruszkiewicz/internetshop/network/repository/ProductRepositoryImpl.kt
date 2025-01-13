package com.andruszkiewicz.internetshop.network.repository

import android.util.Log
import com.andruszkiewicz.internetshop.domain.enums.UserStatus
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.QuantityModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.network.dto.OrderProductRequest
import com.andruszkiewicz.internetshop.network.dto.UserRequest
import com.andruszkiewicz.internetshop.network.service.ProductService
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val service: ProductService
): ProductRepository {

    private val TAG = ProductRepositoryImpl::class.java.simpleName

    override suspend fun createProduct(product: ProductModel) {}

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

    override suspend fun createUser(
        email: String,
        password: String,
        status: UserStatus
    ): UserModel? =
        try {
            service.postUser(
                UserRequest(
                    email,
                    password,
                    status.name
                )
            ).body()?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "createUser: error: $e")
            null
        }

    override suspend fun createEditProduct(product: ProductModel): ProductModel? =
        try {
            service.createEditProduct(
                product.toDto()
            ).body()?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "createEditProduct: error: $e")
            null
        }

    override suspend fun removeProduct(productId: Long): ProductModel? =
        try {
            service
                .removeProduct(productId)
                .body()
                ?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "removeProduct: error: $e")
            null
        }


    override suspend fun logInUser(email: String, password: String): UserModel? =
        try {
            service.logIn(email, password)
                .body()
                ?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "logInUser: error: $e")
            null
        }

    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): UserModel? =
        try {
            Log.d(TAG, "changePassword: email: $email")
            Log.d(TAG, "changePassword: oldPassword: $oldPassword")
            Log.d(TAG, "changePassword: newPassword: $newPassword")
            service.changePassword(email, oldPassword, newPassword)
                .body()
                ?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "changePassword: error: $e")
            null
        }

    override suspend fun buyOrder(id: Long): Boolean =
        service
            .buyOrder(id)
            .isSuccessful
}