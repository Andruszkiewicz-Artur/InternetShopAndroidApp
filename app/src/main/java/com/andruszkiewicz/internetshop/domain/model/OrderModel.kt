package com.andruszkiewicz.internetshop.domain.model

data class OrderModel(
    val id: Long,
    val products: List<QuantityModel>
)
