package com.kfouri.cryptoprice2.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kfouri.cryptoprice2.MainActivity2
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.domain.state.DataState
import com.kfouri.cryptoprice2.ext.hideInProgress
import com.kfouri.cryptoprice2.ext.showError
import com.kfouri.cryptoprice2.ext.showInProgress
import com.kfouri.cryptoprice2.ui.viewmodel.CurrenciesListViewModel
import dagger.hilt.android.AndroidEntryPoint

private val TAG = CurrenciesListFragment::class.java.simpleName

@AndroidEntryPoint
class CurrenciesListFragment: Fragment() {

    private val viewModel: CurrenciesListViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currencies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        viewModel.getAllCurrencies()
    }

    private fun setObserver() {

        viewModel.onDone.observe(viewLifecycleOwner, Observer {

            val intent = Intent(this.context, MainActivity2::class.java)
            startActivity(intent)

        })

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
    }
}