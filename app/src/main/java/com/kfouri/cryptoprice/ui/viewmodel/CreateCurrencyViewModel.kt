package com.kfouri.cryptoprice.ui.viewmodel

import androidx.databinding.ObservableField
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
class CreateCurrencyViewModel
@Inject
constructor(
    private val getCurrencyNetworkUseCase: GetCurrencyNetworkUseCase,
    private val insertUpdateCurrencyUseCase: InsertUpdateCurrencyUseCase,
    private val removeCurrencyUseCase: RemoveCurrencyUseCase,
): ViewModel() {

    private val finishLiveData = MutableLiveData<Unit>()
    private val buttonSaveStatusMutableLiveData = MutableLiveData<Boolean>()
    val buttonSaveStatusLiveData: LiveData<Boolean>
    get() {
        return buttonSaveStatusMutableLiveData
    }

    val onFinish = finishLiveData
    lateinit var currencyId: String
    lateinit var currencySymbol: String
    lateinit var currencyName: String

    val symbolObservable = ObservableField<String>()
    val currentPriceObservable = ObservableField<String>()
    val exchangeObservable = ObservableField<String>()
    val amountObservable = ObservableField<String>()
    val iconObservable = ObservableField<String>()
    val progressBarVisibilityObservable = ObservableField<Boolean>()

    private val chartCurrencyMutableLiveData = MutableLiveData<ChartCurrency>()
    val chartCurrencyLiveData: LiveData<ChartCurrency>
        get() {
            return chartCurrencyMutableLiveData
        }

    private val infoCurrencyMutableLiveData = MutableLiveData<Unit>()
    val infoCurrencyLiveData: LiveData<Unit>
        get() {
            return infoCurrencyMutableLiveData
        }
    private var currentPrice = 0.0
    private var mIcon: String = ""
    private var mCurrentCurrency: Currency

    init {
        progressBarVisibilityObservable.set(false)
        symbolObservable.set("")
        currentPriceObservable.set("")
        exchangeObservable.set("")
        amountObservable.set("")

        mCurrentCurrency = Currency(
                0,
                "",
                "",
                "",
                "",
                0.0,
                0.0,
                0.0,
            "",
                0.0,
                "",
                0.0,
                0)
    }

    fun getNetworkCurrency() {
        progressBarVisibilityObservable.set(true)
        viewModelScope.launch {
            getCurrencyNetworkUseCase.getCurrencyNetwork(currencyId).onEach { data ->
                val list = data.peekDataOrNull()
                list?.let {
                    val format = NumberFormat.getCurrencyInstance(Locale.US)
                    if (it[0].price < 1) {
                        currentPriceObservable.set("$"+it[0].price.toBigDecimal().setScale(8, RoundingMode.UP))
                    } else {
                        currentPriceObservable.set(format.format(it[0].price).toString())
                    }
                    currentPrice = it[0].price
                    iconObservable.set(it[0].icon)
                    mIcon = it[0].icon
                    progressBarVisibilityObservable.set(false)
                    buttonSaveStatusMutableLiveData.postValue(true)
                } ?: run {
                    currentPriceObservable.set("N/A")
                    iconObservable.set(null)
                    progressBarVisibilityObservable.set(false)
                    buttonSaveStatusMutableLiveData.postValue(false)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun saveCurrencyClick(count: Int) {

        viewModelScope.launch {

            var am = 0.0
            var currentPr = 0.0

            if (!amountObservable.get().isNullOrEmpty()) {
                am = amountObservable.get()?.toDouble()?: run { 0.0 }
            }

            if (currentPriceObservable.get() != "N/A") {
                currentPr = currentPrice
            }

            mCurrentCurrency.apply {
                id = currencyId
                symbol = symbolObservable.get().toString()
                exchange = exchangeObservable.get().toString()
                amount = am
                purchasePrice = 0.0
                currentPrice = currentPr
                oldPrice = currentPr
                icon = mIcon
                name = currencyName
                position = count + 1
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
}