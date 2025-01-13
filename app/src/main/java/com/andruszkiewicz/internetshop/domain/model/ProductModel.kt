package com.andruszkiewicz.internetshop.domain.model

import android.os.Parcelable
import com.andruszkiewicz.internetshop.network.dto.ProductDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val id: Long,
    var name: String,
    var prize: Float
) : Parcelable {

    fun toDto() =
        ProductDto(
            id = id,
            name = name,
            prize = prize
        )

}
