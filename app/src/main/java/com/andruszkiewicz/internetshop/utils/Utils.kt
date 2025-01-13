package com.andruszkiewicz.internetshop.utils

import android.content.Context
import android.widget.Toast

object Utils {

    val BASE_URL = "http://10.0.2.2:8080/api/"

    fun toast(message: String, context: Context) =
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    val PRODUCT_EXTRA = "PRODUCT_EXTRA"
}