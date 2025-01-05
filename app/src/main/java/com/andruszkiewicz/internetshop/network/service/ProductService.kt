package com.andruszkiewicz.internetshop.network.service

import com.andruszkiewicz.internetshop.network.dto.OrderProductRequest
import com.andruszkiewicz.internetshop.network.dto.ProductDto
import com.andruszkiewicz.internetshop.network.dto.ProductRequest
import com.andruszkiewicz.internetshop.network.dto.QuantityDto
import com.andruszkiewicz.internetshop.network.dto.UserDto
import com.andruszkiewicz.internetshop.network.dto.UserRequest
import com.andruszkiewicz.internetshop.utils.Utils
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @GET("product")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("user")
    suspend fun getUsers(): Response<List<UserDto>>

    @POST("order/product")
    suspend fun postProduct(@Body orderProduct: OrderProductRequest): Response<QuantityDto>

    @DELETE("order/product")
    suspend fun deleteProduct(@Query("id") productId: Long): Response<Unit>

    @POST("user")
    suspend fun postUser(@Body user: UserRequest): Response<Unit>

    @POST("product")
    suspend fun createProduct(@Body productRequest: ProductRequest): Response<Utils>
}