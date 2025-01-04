package com.andruszkiewicz.internetshop.utils.mapper

fun Float.toPrize() = String.format("%.2f", this) + " zł"