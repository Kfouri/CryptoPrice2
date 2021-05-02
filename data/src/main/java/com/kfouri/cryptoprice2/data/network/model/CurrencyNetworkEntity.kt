package com.kfouri.cryptoprice2.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyNetworkEntity (
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("icon") val icon: String,
    @SerializedName("open24") val open24: Double,
)