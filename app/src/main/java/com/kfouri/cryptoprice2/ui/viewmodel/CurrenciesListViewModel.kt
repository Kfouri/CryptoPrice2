package com.kfouri.cryptoprice2.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.state.DataState
import com.kfouri.cryptoprice2.domain.usecase.GetAllCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class CurrenciesListViewModel
@Inject
constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
): ViewModel() {

    private val getAllCurrenciesLiveData = MutableLiveData<DataState<List<Currency>>>()
    val onGetAllCurrencies = getAllCurrenciesLiveData

    val balanceObservable = ObservableField<String>()
    val investedObservable = ObservableField<String>()
    val earnedObservable = ObservableField<String>()


    init {
        balanceObservable.set("$0.00")
        investedObservable.set("$0.00")
        earnedObservable.set("$0.00")
    }

    fun getAllCurrencies() {
        viewModelScope.launch {

            getAllCurrenciesUseCase.getAllCurrencies().onEach { it ->
                val list = it.peekDataOrNull()
                if (list != null) {
                    val data = DataState.Success(list)
                    val currencyList = data.data

                    var balance = 0.0
                    var totalInvested = 0.0

                    currencyList?.forEach { item ->
                        balance += item.currentPrice * item.amount
                        totalInvested += item.purchasePrice * item.amount
                    }

                    refreshHeader(totalInvested, balance)

                    getAllCurrenciesLiveData.value = data
                } else {
                    getAllCurrenciesLiveData.value = it
                }
            }.launchIn(viewModelScope)

        }
    }

    fun refreshHeader(totalInvested: Double, balance: Double) {
        val totalEarned = balance - totalInvested
        val format = NumberFormat.getCurrencyInstance()

        balanceObservable.set(format.format(balance).toString())
        investedObservable.set(format.format(totalInvested).toString())
        earnedObservable.set(format.format(totalEarned).toString())
    }
}