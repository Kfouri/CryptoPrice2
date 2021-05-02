package com.kfouri.cryptoprice2.domain.model

data class Currency(
        val _id: Int = 0,
        val id: String,
        var symbol: String,
        val name: String,
        var exchange: String = "",
        var amount: Double = 0.0,
        var purchasePrice: Double = 0.0,
        var currentPrice: Double = 0.0,
        var oldPrice: Double = 0.0,
        var icon: String,
        var open24: Double = 0.0
)