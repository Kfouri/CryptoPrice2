package com.kfouri.cryptoprice.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfouri.cryptoprice.domain.model.ChartCurrency
import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ShowCurrencyViewModel
@Inject
constructor(
    private val getCurrencyNetworkUseCase: GetCurrencyNetworkUseCase,
    private val removeCurrencyUseCase: RemoveCurrencyUseCase,
    private val getCurrencyByIdUseCase: GetCurrencyByIdUseCase,
    private val getChartCurrencyUseCase: GetChartCurrencyUseCase,
    private val insertUpdateCurrencyUseCase: InsertUpdateCurrencyUseCase
): ViewModel() {

    private lateinit var mCurrentCurrency: Currency
    lateinit var mCurrencyId: String

    private val finishLiveData = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit>
        get() {
            return finishLiveData
        }

    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() {
            return loadingMutableLiveData
        }

    private val chartCurrencyMutableLiveData = MutableLiveData<ChartCurrency>()
    val chartCurrencyLiveData: LiveData<ChartCurrency>
        get() {
            return chartCurrencyMutableLiveData
        }

    private val infoCurrencyMutableLiveData = MutableLiveData<Currency>()
    val infoCurrencyLiveData: LiveData<Currency>
        get() {
            return infoCurrencyMutableLiveData
        }

    private val editCurrencyMutableLiveData = MutableLiveData<Currency>()
    val editCurrencyLiveData: LiveData<Currency>
        get() {
            return editCurrencyMutableLiveData
        }

    fun getCurrencyById(id: Int) {
        loadingMutableLiveData.postValue(true)
        viewModelScope.launch {
            getCurrencyByIdUseCase.getCurrencyById(id).onEach { it -> //Recupera currency local
                val currency = it.peekDataOrNull()
                if (currency != null) {
                    mCurrentCurrency = currency
                    mCurrencyId = currency.id
                    getNetworkCurrency() //recupera currency Coingecko
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getNetworkCurrency() {
        viewModelScope.launch {
            getCurrencyNetworkUseCase.getCurrencyNetwork(mCurrentCurrency.id).onEach { data ->
                val list = data.peekDataOrNull()
                list?.let {
                    val format = NumberFormat.getCurrencyInstance(Locale.US)
                    if (it[0].price < 1) {
                        mCurrentCurrency.currentPriceFormated = "$"+it[0].price.toBigDecimal().setScale(8, RoundingMode.UP)
                    } else {
                        mCurrentCurrency.currentPriceFormated = format.format(it[0].price).toString()
                    }

                    mCurrentCurrency.currentPrice = it[0].price
                    mCurrentCurrency.icon = it[0].icon

                    mCurrentCurrency.open24 = (it[0].open24).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()

                    val total = mCurrentCurrency.amount*mCurrentCurrency.currentPrice
                    if (total > 0 && total < 1) {
                        mCurrentCurrency.totalFormated = "$"+total.toBigDecimal().setScale(8, RoundingMode.UP)
                    } else {
                        mCurrentCurrency.totalFormated = format.format(total).toString()
                    }

                    mCurrentCurrency.marketCap = "#"+ it[0].marketCapRank.toString()

                    if (it[0].high24h < 1) {
                        mCurrentCurrency.high24Formated = "$"+it[0].high24h.toBigDecimal().setScale(8, RoundingMode.UP)
                    } else {
                        mCurrentCurrency.high24Formated = format.format(it[0].high24h).toString()
                    }

                    if (it[0].low24h < 1) {
                        mCurrentCurrency.low24Formated = "$"+it[0].low24h.toBigDecimal().setScale(8, RoundingMode.UP)
                    } else {
                        mCurrentCurrency.low24Formated = format.format(it[0].low24h).toString()
                    }
                } ?: run {
                    mCurrentCurrency.currentPriceFormated = "N/A"
                    mCurrentCurrency.icon = ""
                    mCurrentCurrency.open24 = 0.00
                    mCurrentCurrency.totalFormated = "$0.00"
                    mCurrentCurrency.marketCap = "# 0"
                    mCurrentCurrency.high24Formated = "$0.00"
                    mCurrentCurrency.low24Formated = "$0.00"
                }
                loadingMutableLiveData.postValue(false)
                infoCurrencyMutableLiveData.postValue(mCurrentCurrency)
            }.launchIn(viewModelScope)
        }
    }

    fun removeCurrency() {
        viewModelScope.launch {
            removeCurrencyUseCase.removeCurrency(mCurrentCurrency)
            finishLiveData.value = Unit
        }
    }

    fun editCurrencyClick(value: String) {

        viewModelScope.launch {

            var am = 0.0

            if (value.isNotEmpty()) {
                am = value.toDouble()
            }

            mCurrentCurrency.amount = am

            insertUpdateCurrencyUseCase.insertUpdateCurrency(mCurrentCurrency)

            val format = NumberFormat.getCurrencyInstance(Locale.US)
            val total = mCurrentCurrency.amount*mCurrentCurrency.currentPrice
            if (total > 0 && total < 1) {
                mCurrentCurrency.totalFormated = "$"+total.toBigDecimal().setScale(8, RoundingMode.UP)
            } else {
                mCurrentCurrency.totalFormated = format.format(total).toString()
            }
            editCurrencyMutableLiveData.postValue(mCurrentCurrency)
        }
    }

    fun getChartCurrency(symbol: String, days: String) {
        viewModelScope.launch {
            getChartCurrencyUseCase.getChartCurrency(symbol, days).onEach { data ->
                val info = data.peekDataOrNull()
                if (info != null) {
                    chartCurrencyMutableLiveData.postValue(info!!)
                }
            }.launchIn(viewModelScope)
        }
    }
}