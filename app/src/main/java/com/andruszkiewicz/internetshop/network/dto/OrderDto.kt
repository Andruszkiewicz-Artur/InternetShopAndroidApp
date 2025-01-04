package com.andruszkiewicz.internetshop.network.dto

import com.andruszkiewicz.internetshop.domain.model.OrderModel

data class OrderDto(
    val id: Long,
    val products: List<QuantityDto>
) {
    fun toDomain() = OrderModel(
        id = id,
        products = products.map { it.toDomain() }
    )
}
