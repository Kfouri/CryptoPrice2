package com.kfouri.cryptoprice.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyNetworkEntity (
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("price") val price: Double,
    @SerializedName("icon") val icon: String,
    @SerializedName("open24") val open24: Double,
    @SerializedName("market_cap_rank") val marketCapRank: Int,
    @SerializedName("high_24h") val high24h: Double,
    @SerializedName("low_24h") val low24h: Double,
)