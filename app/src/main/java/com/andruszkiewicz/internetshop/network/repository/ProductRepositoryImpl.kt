package com.andruszkiewicz.internetshop.network.repository

import android.util.Log
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.model.UserModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.network.service.ProductService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val service: ProductService
): ProductRepository {

    private val TAG = ProductRepositoryImpl::class.java.simpleName

    override suspend fun createProduct(product: ProductModel) {  }

    override suspend fun getProducts(): Flow<List<ProductModel>> {
        val response = service.getProducts()

        Log.d(TAG, response.toString())
        Log.d(TAG, response.body().toString())

        return flow {
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it.map { productDto -> productDto.toDomain() })
                } ?: emit(emptyList())
            } else {
                // Log or handle unsuccessful responses
                emit(emptyList())  // Emit empty list if response is not successful
                Log.e(TAG, "Failed to fetch products: ${response.errorBody()}")
            }
        }
    }

    override suspend fun getUsers(): Flow<List<UserModel>> {
        val response = service.getUsers()

        Log.d(TAG, response.toString())
        Log.d(TAG, response.body().toString())

        return flow {
            if(response.isSuccessful) {
                response.body()?.let {
                    emit(it.map { userDto -> userDto.toDomain() })
                } ?: emit(emptyList())
            } else {
                emit(emptyList())
                Log.e(TAG, "Failed to fetch users: ${response.errorBody()}")
            }
        }
    }

}