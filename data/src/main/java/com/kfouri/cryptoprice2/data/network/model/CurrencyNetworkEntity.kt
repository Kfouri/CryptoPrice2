package com.kfouri.cryptoprice2.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyNetworkEntity (
    @SerializedName("USDT") val usdt: Float,
)