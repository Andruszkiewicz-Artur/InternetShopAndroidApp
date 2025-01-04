package com.andruszkiewicz.internetshop.network.dto

import com.andruszkiewicz.internetshop.domain.model.QuantityModel

data class QuantityDto(
    val id: Long,
    val quantity: Int,
    val product: ProductDto
) {

    fun toDomain() = QuantityModel(
            id = id,
            quantity = quantity,
            product = product.toDomain()
        )

}