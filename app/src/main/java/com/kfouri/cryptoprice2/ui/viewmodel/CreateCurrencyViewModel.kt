package com.kfouri.cryptoprice2.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.usecase.GetCurrencyByIdUseCase
import com.kfouri.cryptoprice2.domain.usecase.GetCurrencyNetworkUseCase
import com.kfouri.cryptoprice2.domain.usecase.InsertUpdateCurrencyUseCase
import com.kfouri.cryptoprice2.domain.usecase.RemoveCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CreateCurrencyViewModel
@Inject
constructor(
    private val getCurrencyNetworkUseCase: GetCurrencyNetworkUseCase,
    private val insertUpdateCurrencyUseCase: InsertUpdateCurrencyUseCase,
    private val removeCurrencyUseCase: RemoveCurrencyUseCase,
    private val getCurrencyByIdUseCase: GetCurrencyByIdUseCase
): ViewModel() {

    private val finishLiveData = MutableLiveData<Unit>()
    val onFinish = finishLiveData
    lateinit var currencyId: String
    lateinit var currencySymbol: String
    lateinit var currencyName: String

    val symbolObservable = ObservableField<String>()
    val currentPriceObservable = ObservableField<String>()
    val exchangeObservable = ObservableField<String>()
    val amountObservable = ObservableField<String>()
    val purchasePriceObservable = ObservableField<String>()
    val totalInvestedObservable = ObservableField<String>()
    val totalCurrentObservable = ObservableField<String>()
    val iconObservable = ObservableField<String>()
    val progressBarVisibilityObservable = ObservableField<Boolean>()
    val buttonSaveObservable = ObservableField<Boolean>()

    private var mIcon: String = ""
    private var mCurrentCurrency: Currency

    init {
        buttonSaveObservable.set(false)
        progressBarVisibilityObservable.set(false)
        symbolObservable.set("")
        currentPriceObservable.set("")
        exchangeObservable.set("")
        amountObservable.set("")
        purchasePriceObservable.set("")
        totalInvestedObservable.set("$0.00")
        totalCurrentObservable.set("$0.00")

        mCurrentCurrency = Currency(
                0,
                "",
                "",
                "",
                "",
                0.0,
                0.0,
                0.0,
                0.0,
                "")
    }

    fun getNetworkCurrency() {
        progressBarVisibilityObservable.set(true)
        viewModelScope.launch {
            getCurrencyNetworkUseCase.getCurrencyNetwork(currencyId).onEach { data ->
                val list = data.peekDataOrNull()
                list?.let {
                    currentPriceObservable.set(BigDecimal.valueOf(it[0].price).toPlainString())
                    iconObservable.set(it[0].icon)
                    mIcon = it[0].icon
                    progressBarVisibilityObservable.set(false)
                    buttonSaveObservable.set(true)
                } ?: run {
                    currentPriceObservable.set("N/A")
                    iconObservable.set(null)
                    progressBarVisibilityObservable.set(false)
                    buttonSaveObservable.set(false)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun saveCurrencyClick() {

        viewModelScope.launch {

            var am = 0.0
            var purchasePr = 0.0
            var currentPr = 0.0

            if (!amountObservable.get().isNullOrEmpty()) {
                am = amountObservable.get()?.toDouble()?: run { 0.0 }
            }

            if (!purchasePriceObservable.get().isNullOrEmpty()) {
                purchasePr = purchasePriceObservable.get()?.toDouble()?: run { 0.0 }
            }

            if (currentPriceObservable.get() != "N/A") {
                currentPr = currentPriceObservable.get()?.toDouble() ?: run { 0.0 }
            }

            mCurrentCurrency.apply {
                id = currencyId
                symbol = symbolObservable.get().toString()
                exchange = exchangeObservable.get().toString()
                amount = am
                purchasePrice = purchasePr
                currentPrice = currentPr
                oldPrice = currentPr
                icon = mIcon
            }

            insertUpdateCurrencyUseCase.insertUpdateCurrency(mCurrentCurrency)
            finishLiveData.value = Unit
        }
    }

    fun removeCurrency() {
        viewModelScope.launch {
            removeCurrencyUseCase.removeCurrency(mCurrentCurrency)
            finishLiveData.value = Unit
        }
    }

    fun getCurrencyById(id: Int) {
        progressBarVisibilityObservable.set(true)
        viewModelScope.launch {
            getCurrencyByIdUseCase.getCurrencyById(id).onEach { it ->
                val currency = it.peekDataOrNull()
                if (currency != null) {
                    mCurrentCurrency = currency

                    symbolObservable.set(mCurrentCurrency.symbol)
                    exchangeObservable.set(mCurrentCurrency.exchange)
                    amountObservable.set(BigDecimal.valueOf(mCurrentCurrency.amount).toPlainString())
                    purchasePriceObservable.set(BigDecimal.valueOf(mCurrentCurrency.purchasePrice).toPlainString())
                    totalInvestedObservable.set("$"+(mCurrentCurrency.purchasePrice * mCurrentCurrency.amount).toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString())
                    totalCurrentObservable.set("$"+(mCurrentCurrency.currentPrice * mCurrentCurrency.amount).toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString())
                    currencyId = mCurrentCurrency.id
                    getNetworkCurrency()
                }
                progressBarVisibilityObservable.set(false)
            }.launchIn(viewModelScope)
        }
    }
}