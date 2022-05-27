package com.kfouri.cryptoprice.data.network.service

import com.kfouri.cryptoprice.data.network.model.ChartCurrencyEntity
import com.kfouri.cryptoprice.data.network.model.CurrencyAvailableNetworkEntity
import com.kfouri.cryptoprice.data.network.model.CurrencyCoinGeckoResponse
import com.kfouri.cryptoprice.data.network.model.CurrencyResponse
import com.kfouri.cryptoprice.domain.model.ChartCurrency
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyService {

    companion object {
        private const val CURRENCY_COMP = "USDT"
        private const val CURRENCY_PATH = "data/pricemultifull?tsyms=$CURRENCY_COMP"//&e=$EXCHANGE"
        private const val CURRENCY_LIST_PATH = "api/v3/coins/list?include_platform=false"
        private const val CURRENCY_COINGECKO_PATH = "api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false"
        private const val CHART_CURRENCY_COINGECKO_PATH = "api/v3/coins/{idCurrency}/market_chart?vs_currency=usd"
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

    @GET(CHART_CURRENCY_COINGECKO_PATH)
    suspend fun getChartCurrency(
        @Path("idCurrency") idCurrency: String,
        @Query("days") days: String = "1"
    ): ChartCurrencyEntity
}