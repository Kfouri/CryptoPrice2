package com.kfouri.cryptoprice2.data.network.service

import com.kfouri.cryptoprice2.data.network.model.CurrencyAvailableNetworkEntity
import com.kfouri.cryptoprice2.data.network.model.CurrencyCoinGeckoResponse
import com.kfouri.cryptoprice2.data.network.model.CurrencyNetworkEntity
import com.kfouri.cryptoprice2.data.network.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyService {

    companion object {
        private const val CURRENCY_NAME = "currency_name"
        private const val CURRENCY_PATH2 = "api/v3/coins/{name}?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false"
        private const val CURRENCY_COMP = "USDT"
        private const val EXCHANGE = "Binance"
        private const val SYMBOL = "symbol"
        private const val CURRENCY_PATH = "data/pricemultifull?tsyms=$CURRENCY_COMP"//&e=$EXCHANGE"
        private const val CURRENCY_LIST_PATH = "api/v3/coins/list?include_platform=false"
        private const val CURRENCY_COINGECKO_PATH = "api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false"
    }

    @GET(CURRENCY_PATH)
    suspend fun getPrice(
            @Query("fsyms") symbol: String
    ): CurrencyResponse

    @GET(CURRENCY_LIST_PATH)
    suspend fun getAvailableCurrencyList(): List<CurrencyAvailableNetworkEntity>

    @GET(CURRENCY_COINGECKO_PATH)
    suspend fun getCurrenciesCoinGecko(
        @Query("ids") ids: String
    ): List<CurrencyCoinGeckoResponse>
}