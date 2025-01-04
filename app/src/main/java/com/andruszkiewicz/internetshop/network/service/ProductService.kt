package com.andruszkiewicz.internetshop.network.service

import com.andruszkiewicz.internetshop.network.dto.ProductDto
import com.andruszkiewicz.internetshop.network.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("product")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("user")
    suspend fun getUsers(): Response<List<UserDto>>
}