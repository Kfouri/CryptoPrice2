package com.kfouri.cryptoprice2.data.repository

import android.util.Log
import com.kfouri.cryptoprice2.data.db.datasourceimpl.CurrencyLocalDataSource
import com.kfouri.cryptoprice2.data.network.datasourceimpl.CurrencyNetworkDataSource
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
import com.kfouri.cryptoprice2.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class CurrencyRepositoryImpl
@Inject
constructor(
        private val currencyLocalDataSource: CurrencyLocalDataSource,
        private val currencyNetworkDataSource: CurrencyNetworkDataSource,
): CurrencyRepository {

    override suspend fun getCurrencyById(id: Int): Flow<DataState<Currency>> = flow {
        emit(DataState.InProgress())
        try {
            val currency = currencyLocalDataSource.getCurrency(id)
            emit(DataState.Success(currency))
        } catch (e: Exception) {
            emit(DataState.Failure<Currency>(e))
        }
    }

    override suspend fun getAllCurrencies(): Flow<DataState<List<Currency>>> = flow {
        emit(DataState.InProgress())
        try {
            val list = currencyLocalDataSource.getAllCurrencies()
            var currencyNames = ""

            list.forEach {
                it.oldPrice = it.currentPrice
                currencyLocalDataSource.insertUpdateCurrency(it)
                currencyNames = currencyNames + it.symbol + ","
            }

            currencyNames = currencyNames.substring(0, currencyNames.length - 1)

            val currencyList = currencyNetworkDataSource.getCurrencies(currencyNames)
            currencyList.forEach {
                Log.d("Kafu", "Names: "+it.name+" Price:"+it.price+" icon: "+it.icon)
            }

            list.forEach { l ->
                val c = currencyList.find { l.symbol.compareTo(it.name,true) == 0 }
                l.currentPrice = c?.price?:0F
                l.icon = c?.icon?:""

                currencyLocalDataSource.insertUpdateCurrency(l)
            }
            /*
            runBlocking {
                try {
                    val currency = currencyNetworkDataSource.getCurrency(it.id)
                    it.currentPrice = currency.price
                    it.icon = currency.icon
                    Log.d("Kafu", "$it.name")
                } catch (e: InterruptedIOException) {
                    it.currentPrice = 0F
                    it.icon = ""
                    Log.d("Kafu", "$it.name TIMEOUT!!!!!!!")
                }
            }
             */
            emit(DataState.Success(list))
        } catch (e: Exception) {
            emit(DataState.Failure<List<Currency>>(e))
        }
    }

    override suspend fun insertUpdateCurrency(currency: Currency) {
        currencyLocalDataSource.insertUpdateCurrency(currency)
        currencyLocalDataSource.getAllCurrencies()
    }

    override suspend fun removeCurrency(currency: Currency) {
        currencyLocalDataSource.removeCurrency(currency)
        currencyLocalDataSource.getAllCurrencies()
    }

    override suspend fun getCurrenciesAvailable(): Flow<DataState<List<CurrencyAvailableNetwork>>> = flow {
        emit(DataState.InProgress())
        try {
            val list = currencyNetworkDataSource.getAvailableCurrencyList()
            emit(DataState.Success(list))
        } catch (e: Exception) {
            emit(DataState.Failure<List<CurrencyAvailableNetwork>>(e))
        }
    }

}