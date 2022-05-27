package com.kfouri.cryptoprice.domain.model

data class CurrencyNetwork (
    val name: String,
    val symbol: String,
    val price: Double,
    val icon: String,
    val open24: Double,
    val marketCapRank: Int,
    val high24h: Double,
    val low24h: Double
)