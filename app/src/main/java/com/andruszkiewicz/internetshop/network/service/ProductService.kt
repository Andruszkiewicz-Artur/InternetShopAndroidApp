package com.andruszkiewicz.internetshop.network.service

import com.andruszkiewicz.internetshop.network.dto.OrderProductRequest
import com.andruszkiewicz.internetshop.network.dto.ProductDto
import com.andruszkiewicz.internetshop.network.dto.QuantityDto
import com.andruszkiewicz.internetshop.network.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {

    @GET("product")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("user")
    suspend fun getUsers(): Response<List<UserDto>>

    @POST("order/product")
    suspend fun postProduct(@Body orderProduct: OrderProductRequest): Response<QuantityDto>

}