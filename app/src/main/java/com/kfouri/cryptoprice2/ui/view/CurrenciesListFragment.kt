package com.kfouri.cryptoprice2.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.domain.state.DataState
import com.kfouri.cryptoprice2.ext.hideInProgress
import com.kfouri.cryptoprice2.ext.showError
import com.kfouri.cryptoprice2.ext.showInProgress
import com.kfouri.cryptoprice2.ui.viewmodel.CurrenciesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Singleton

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
        viewModel.onGetAllCurrencies.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideInProgress()
                    val currenciesList = dataState.data
                    //Log.d("Kafu", "Cantidad: "+ currenciesList?.size)
                    currenciesList?.forEach {
                        Log.d("Kafu", it.name + " " + it.currentPrice)
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