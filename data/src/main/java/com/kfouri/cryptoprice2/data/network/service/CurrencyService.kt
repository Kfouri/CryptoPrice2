package com.kfouri.cryptoprice2.data.network.service

import com.kfouri.cryptoprice2.data.network.model.CurrencyNetworkEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    companion object {
        private const val CURRENCY_PATH = "data/price?tsyms=USDT"
        private const val CURRENCY_QUERY = "fsym"
    }

    @GET(CURRENCY_PATH)
    suspend fun getPrice(
            @Query(CURRENCY_QUERY) currency: String
    ): CurrencyNetworkEntity
}