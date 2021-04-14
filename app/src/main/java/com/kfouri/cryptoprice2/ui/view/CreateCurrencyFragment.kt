package com.kfouri.cryptoprice2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.ui.viewmodel.CreateCurrencyViewModel

class CreateCurrencyFragment: Fragment() {

    private val viewModel: CreateCurrencyViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
    }

    private fun setObserver() {
        /*
        viewModel.onCurrenciesAvailable.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    val currenciesAvailableList = dataState.data
                    Log.d("Kafu", "Cantidad: "+ currenciesAvailableList?.size)
                }
                is DataState.Failure -> {
                    Log.e(TAG, "Failure", dataState.error)
                    showError(dataState.error)
                }
                is DataState.InProgress -> {
                    Log.d(TAG, "In progress")
                }
            }
        })

        viewModel.onGetAllCurrencies.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideInProgress()
                    val currenciesList = dataState.data
                    //Log.d("Kafu", "Cantidad: "+ currenciesList?.size)
                    currenciesList?.forEach {
                        Log.d("Kafu", "Nombre: " + it.name + " Price: " + it.currentPrice + " Old: " + it.oldPrice + " icon: "+it.icon)
                    }
                }
                is DataState.Failure -> {
                    hideInProgress()
                    Log.e(TAG, "Failure", dataState.error)
                    showError(dataState.error)
                }
                is DataState.InProgress -> {
                    showInProgress()
                    Log.d(TAG, "In progress")
                }
            }
        })

         */
    }


}