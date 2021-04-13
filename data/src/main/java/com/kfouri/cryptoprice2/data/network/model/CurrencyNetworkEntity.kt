package com.kfouri.cryptoprice2.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyNetworkEntity (
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: ImageCurrencyNetworkEntity,
    @SerializedName("market_data") val marketData: MarketDataCurrencyNetworkEntity,
)

data class ImageCurrencyNetworkEntity (
    @SerializedName("thumb") val thumb: String,
    @SerializedName("small") val small: String,
    @SerializedName("large") val large: String,
)

data class MarketDataCurrencyNetworkEntity (
    @SerializedName("current_price") val currentPrice: CurrentPriceCurrencyNetworkEntity
)

data class CurrentPriceCurrencyNetworkEntity (
    @SerializedName("usd") val usd: Float
)
