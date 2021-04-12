package com.kfouri.cryptoprice2.domain.model

data class Currency(
    val id: Int,
    val name: String,
    val exchange: String = "",
    val amount: Float,
    val purchasePrice: Float = 0F,
    var currentPrice: Float = 0F,
    var oldPrice: Float = 0F,
)