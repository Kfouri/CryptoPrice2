package com.kfouri.cryptoprice2.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyNetworkEntity (
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Float,
    @SerializedName("icon") val icon: String,
)