package com.kfouri.cryptoprice.data.network.datasourceimpl

import com.kfouri.cryptoprice.data.network.mapper.ChartCurrencyMapper
import com.kfouri.cryptoprice.data.network.mapper.CurrencyAvailableNetworkMapper
import com.kfouri.cryptoprice.data.network.mapper.CurrencyNetworkMapper
import com.kfouri.cryptoprice.data.network.model.CurrencyNetworkEntity
import com.kfouri.cryptoprice.data.network.service.CurrencyService
import com.kfouri.cryptoprice.data.network.utils.NetworkConstants.BASE_IMAGE
import com.kfouri.cryptoprice.domain.datasource.DataSourceNetwork
import com.kfouri.cryptoprice.domain.model.ChartCurrency
import com.kfouri.cryptoprice.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice.domain.model.CurrencyNetwork
import com.kfouri.cryptoprice.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CurrencyNetworkDataSource
@Inject
constructor(
        private val currencyService: CurrencyService,
        private val currencyNetworkMapper: CurrencyNetworkMapper,
        private val currencyAvailableNetworkMapper: CurrencyAvailableNetworkMapper,
        private val chartCurrencyMapper: ChartCurrencyMapper
) : DataSourceNetwork<CurrencyNetwork, CurrencyAvailableNetwork, ChartCurrency> {

    override suspend fun getCurrencyCoinGecko(symbols: String): List<CurrencyNetwork> {

        val currencies = currencyService.getCurrenciesCoinGecko(symbols)
        val list = ArrayList<CurrencyNetworkEntity>()

        currencies.forEach {
            val symbol = it.symbol.uppercase()
            val price = it.currentPrice
            val image = it.image
            val open24hr = it.priceChangePercentage24h
            val name = it.name
            val high24 = it.high24h
            val low24 = it.low24h
            val marketCapRank = it.marketCapRank
            list.add(CurrencyNetworkEntity(
                name,
                symbol,
                price,
                image,
                open24hr,
                marketCapRank,
                high24,
                low24))
        }

        return currencyNetworkMapper.toModelList(list)
    }

    override suspend fun getCurrencies(symb: String): List<CurrencyNetwork> {
        var symbols = symb
        val currencies = currencyService.getPrice(symbols).display
        val list = ArrayList<CurrencyNetworkEntity>()
        symbols += ','

        val listPrice = symbols.split(",") as ArrayList<String>
        listPrice.forEach {
            val symbol = it.uppercase()
            val curr = currencies.getAsJsonObject(symbol)
            if (curr != null) {
                val data = curr.getAsJsonObject("USDT")
                val priceTemp = data.getAsJsonPrimitive("PRICE").toString()
                val open24hrTemp = data.getAsJsonPrimitive("OPEN24HOUR").toString()
                val icon = data.getAsJsonPrimitive("IMAGEURL").toString().replace("\"","")
                val price = priceTemp.replace("₮ ", "").replace("\"","").replace(",","").toDouble()
                val open24hr = open24hrTemp.replace("₮ ", "").replace("\"","").replace(",","").toDouble()
                list.add(CurrencyNetworkEntity(symbol, symbol, price, BASE_IMAGE + icon, open24hr, 0, 0.0, 0.0))
            }
        }

        return currencyNetworkMapper.toModelList(list)
    }

    override suspend fun getAvailableCurrencyList(): List<CurrencyAvailableNetwork> {
        return currencyAvailableNetworkMapper.toModelList(currencyService.getAvailableCurrencyList())
    }

    override suspend fun getChartCurrency(symbol: String, days: String): ChartCurrency {
        return chartCurrencyMapper.toModel(currencyService.getChartCurrency(symbol, days))
    }
}