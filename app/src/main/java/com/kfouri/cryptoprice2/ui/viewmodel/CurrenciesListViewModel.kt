package com.kfouri.cryptoprice2.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.state.DataState
import com.kfouri.cryptoprice2.domain.usecase.GetAllCurrenciesUseCase
import com.kfouri.cryptoprice2.domain.usecase.GetCurrenciesAvailableUseCase
import com.kfouri.cryptoprice2.domain.usecase.InsertUpdateCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesListViewModel
@Inject
constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val insertUpdateCurrencyUseCase: InsertUpdateCurrencyUseCase,
    private val getCurrenciesAvailableUseCase: GetCurrenciesAvailableUseCase
): ViewModel() {

    private val getAllCurrenciesLiveData = MutableLiveData<DataState<List<Currency>>>()
    val onGetAllCurrencies = getAllCurrenciesLiveData

    /*
    init {
        if (AvailableCurrencies.getSize() == 0) {
            Log.d("Kafu", "Available vacio")
            getCurrenciesAvailable()
        } else {
            Log.d("Kafu", "Available con datos: "+AvailableCurrencies.getSize())
        }
    }


    private fun getCurrenciesAvailable() {
        viewModelScope.launch {
            getCurrenciesAvailableUseCase.getCurrenciesAvailable().onEach {
                val list = it.peekDataOrNull()
                if (list != null) {
                    AvailableCurrencies.setAvailableList(DataState.Success(list).data as ArrayList<CurrencyAvailableNetwork>)
                    doneLiveData.value = Unit
                }
            }.launchIn(viewModelScope)
        }
    }
     */

    fun getAllCurrencies() {
        viewModelScope.launch {

            /*
            insertUpdateCurrencyUseCase.insertUpdateCurrency(
                    Currency(0,
                            "",
                            "TLM",
                            "",
                            "Exchange",
                            1F,
                            0F,
                            0F,
                            0F,
                            "")
            )
*/
            getAllCurrenciesUseCase.getAllCurrencies().onEach {
                val list = it.peekDataOrNull()
                if (list != null) {
                    getAllCurrenciesLiveData.value = DataState.Success(list)
                } else {
                    getAllCurrenciesLiveData.value = it
                }
            }.launchIn(viewModelScope)

        }
    }
}