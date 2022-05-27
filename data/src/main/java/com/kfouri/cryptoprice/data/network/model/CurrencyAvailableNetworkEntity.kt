package com.kfouri.cryptoprice.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyAvailableNetworkEntity(
        @SerializedName("id") val id: String,
        @SerializedName("symbol") val symbol: String,
        @SerializedName("name") val name: String,
)