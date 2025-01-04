package com.andruszkiewicz.internetshop.domain.model

data class QuantityModel(
    val id: Long,
    val quantity: Int,
    val product: ProductModel
)
