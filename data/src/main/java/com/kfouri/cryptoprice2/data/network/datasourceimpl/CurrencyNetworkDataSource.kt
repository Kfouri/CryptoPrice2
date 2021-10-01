package com.kfouri.cryptoprice2.data.network.datasourceimpl

import com.kfouri.cryptoprice2.data.network.mapper.CurrencyAvailableNetworkMapper
import com.kfouri.cryptoprice2.data.network.mapper.CurrencyNetworkMapper
import com.kfouri.cryptoprice2.data.network.model.CurrencyNetworkEntity
import com.kfouri.cryptoprice2.data.network.service.CurrencyService
import com.kfouri.cryptoprice2.data.network.utils.NetworkConstants.BASE_IMAGE
import com.kfouri.cryptoprice2.domain.datasource.DataSourceNetwork
import com.kfouri.cryptoprice2.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice2.domain.model.CurrencyNetwork
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CurrencyNetworkDataSource
@Inject
constructor(
        private val currencyService: CurrencyService,
        private val currencyNetworkMapper: CurrencyNetworkMapper,
        private val currencyAvailableNetworkMapper: CurrencyAvailableNetworkMapper
) : DataSourceNetwork<CurrencyNetwork, CurrencyAvailableNetwork> {

    override suspend fun getCurrencyCoinGecko(symbols: String): List<CurrencyNetwork> {

        val currencies = currencyService.getCurrenciesCoinGecko(symbols)
        val list = ArrayList<CurrencyNetworkEntity>()

        currencies.forEach {
            val symbol = it.symbol.toUpperCase(Locale.getDefault())
            val price = it.currentPrice
            val image = it.image
            val open24hr = it.priceChangePercentage24h
            list.add(CurrencyNetworkEntity(symbol, price, image, open24hr))
        }

        return currencyNetworkMapper.toModelList(list)
    }

    override suspend fun getCurrencies(symbols: String): List<CurrencyNetwork> {
        var symbols = symbols
        val currencies = currencyService.getPrice(symbols).display
        val list = ArrayList<CurrencyNetworkEntity>()
        symbols += ','

        val listPrice = symbols.split(",") as ArrayList<String>
        listPrice.forEach {
            val symbol = it.toUpperCase(Locale.getDefault())
            val curr = currencies.getAsJsonObject(symbol)
            if (curr != null) {
                val data = curr.getAsJsonObject("USDT")
                val priceTemp = data.getAsJsonPrimitive("PRICE").toString()
                val open24hrTemp = data.getAsJsonPrimitive("OPEN24HOUR").toString()
                val icon = data.getAsJsonPrimitive("IMAGEURL").toString().replace("\"","")
                val price = priceTemp.replace("₮ ", "").replace("\"","").replace(",","").toDouble()
                val open24hr = open24hrTemp.replace("₮ ", "").replace("\"","").replace(",","").toDouble()
                list.add(CurrencyNetworkEntity(symbol, price, BASE_IMAGE + icon, open24hr))
            }
        }

        return currencyNetworkMapper.toModelList(list)
    }

    override suspend fun getAvailableCurrencyList(): List<CurrencyAvailableNetwork> {
        return currencyAvailableNetworkMapper.toModelList(currencyService.getAvailableCurrencyList())
    }
}