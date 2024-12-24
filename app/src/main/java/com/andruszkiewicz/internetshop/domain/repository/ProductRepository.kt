package com.andruszkiewicz.internetshop.domain.repository

import com.andruszkiewicz.internetshop.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun createProduct(product: ProductModel)

    suspend fun getProducts(): Flow<List<ProductModel>>

}