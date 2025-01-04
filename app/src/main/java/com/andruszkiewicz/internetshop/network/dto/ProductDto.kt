package com.andruszkiewicz.internetshop.network.dto

import com.andruszkiewicz.internetshop.domain.model.ProductModel

data class ProductDto(
    val id: Long,
    val name: String,
    val prize: Float
) {
    fun toDomain() = ProductModel(
            id = id,
            name = name,
            prize = prize
        )
}
