package com.kfouri.cryptoprice2.data.network.service

import com.kfouri.cryptoprice2.data.network.model.CurrencyAvailableNetworkEntity
import com.kfouri.cryptoprice2.data.network.model.CurrencyNetworkEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyService {

    companion object {
        private const val CURRENCY_NAME = "currency_name"
        private const val CURRENCY_PATH = "api/v3/coins/{name}?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false"

        private const val CURRENCY_LIST_PATH = "api/v3/coins/list?include_platform=false"
    }

    @GET(CURRENCY_PATH)
    suspend fun getPrice(
            @Path("name") name: String
    ): CurrencyNetworkEntity

    @GET(CURRENCY_LIST_PATH)
    suspend fun getAvailableCurrencyList(): List<CurrencyAvailableNetworkEntity>
}