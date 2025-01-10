package com.andruszkiewicz.internetshop.domain.mapper

fun Float.toPrize() = String.format("%.2f", this) + " zł"