package com.kfouri.cryptoprice2.data.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CurrencyResponse (
    @SerializedName("DISPLAY") val display: JsonObject
)

data class CurrencyCoinGeckoResponse (
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("current_price") val currentPrice: Double,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double,
)