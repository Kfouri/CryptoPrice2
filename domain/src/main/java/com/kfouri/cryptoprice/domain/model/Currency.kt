package com.kfouri.cryptoprice.domain.model

data class Currency(
        val _id: Int = 0,
        var id: String,
        var symbol: String,
        var name: String,
        var exchange: String = "",
        var amount: Double = 0.0,
        var purchasePrice: Double = 0.0,
        var currentPrice: Double = 0.0,
        var currentPriceFormated: String = "",
        var oldPrice: Double = 0.0,
        var icon: String,
        var open24: Double = 0.0,
        var position: Int,
        var totalFormated: String = "",
        var marketCap: String = "",
        var high24Formated: String = "",
        var low24Formated: String = ""
)