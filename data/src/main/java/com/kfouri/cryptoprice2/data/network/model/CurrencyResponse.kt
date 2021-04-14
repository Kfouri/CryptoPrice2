package com.kfouri.cryptoprice2.data.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CurrencyResponse (
    @SerializedName("DISPLAY") val display: JsonObject
)