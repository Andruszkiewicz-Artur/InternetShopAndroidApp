package com.andruszkiewicz.internetshop.network.repository

import android.util.Log
import com.andruszkiewicz.internetshop.domain.model.ProductModel
import com.andruszkiewicz.internetshop.domain.repository.ProductRepository
import com.andruszkiewicz.internetshop.network.service.ProductService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val service: ProductService
): ProductRepository {

    override suspend fun createProduct(product: ProductModel) {  }

    override suspend fun getProducts(): Flow<List<ProductModel>> {
        val response = service.getProducts()

        return flow {
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it.map { productDto -> productDto.toDomain() })
                } ?: emit(emptyList())
            } else {
                // Log or handle unsuccessful responses
                emit(emptyList())  // Emit empty list if response is not successful
                Log.e("ProductRepository_TAG", "Failed to fetch products: ${response.errorBody()}")
            }
        }
    }

}