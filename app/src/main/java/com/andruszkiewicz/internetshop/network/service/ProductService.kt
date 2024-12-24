package com.andruszkiewicz.internetshop.network.service

import com.andruszkiewicz.internetshop.network.dto.ProductDto
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("product")
    suspend fun getProducts(): Response<List<ProductDto>>

}