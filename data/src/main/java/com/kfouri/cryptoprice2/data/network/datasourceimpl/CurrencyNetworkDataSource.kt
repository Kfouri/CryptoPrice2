package com.kfouri.cryptoprice2.data.network.datasourceimpl

import android.util.Log
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

    override suspend fun getCurrencies(symbols: String): List<CurrencyNetwork> {
        var symbols = symbols
        val currencies = currencyService.getPrice(symbols).display
        val list = ArrayList<CurrencyNetworkEntity>()
        symbols += ','

        val listPrice = symbols.split(",") as ArrayList<String>
        Log.d("Kafu", "List size: "+listPrice.size)
        listPrice.forEach {
            val symbol = it.toUpperCase(Locale.getDefault())
            val curr = currencies.getAsJsonObject(symbol)
            if (curr != null) {
                Log.d("Kafu", "curr: "+curr.toString())
                val data = curr.getAsJsonObject("USDT")
                val priceTemp = data.getAsJsonPrimitive("PRICE").toString()
                val icon = data.getAsJsonPrimitive("IMAGEURL").toString().replace("\"","")
                val price = priceTemp.replace("₮ ", "").replace("\"","").replace(",","").toFloat()
                Log.d("Kafu", "Moneda: "+symbol+" Precio: "+price+" Icon: "+BASE_IMAGE + icon)
                list.add(CurrencyNetworkEntity(symbol, price, BASE_IMAGE + icon))
            }
        }

        return currencyNetworkMapper.toModelList(list)
    }

    override suspend fun getAvailableCurrencyList(): List<CurrencyAvailableNetwork> {
        return currencyAvailableNetworkMapper.toModelList(currencyService.getAvailableCurrencyList())
    }
}