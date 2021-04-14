package com.kfouri.cryptoprice2.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.state.DataState
import com.kfouri.cryptoprice2.ext.hideInProgress
import com.kfouri.cryptoprice2.ext.showError
import com.kfouri.cryptoprice2.ext.showInProgress
import com.kfouri.cryptoprice2.ui.adapter.ListAdapter
import com.kfouri.cryptoprice2.ui.viewmodel.CurrenciesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_currencies.*

private val TAG = CurrenciesListFragment::class.java.simpleName

@AndroidEntryPoint
class CurrenciesListFragment: Fragment() {

    private val viewModel: CurrenciesListViewModel by viewModels()
    private val listAdapter = ListAdapter(onClicked())

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currencies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setObserver()
        viewModel.getAllCurrencies()
    }

    private fun setLayout() {
        setAppBar()

        recycler_view.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = listAdapter
        }
    }

    private fun setAppBar() {
        (activity as? MainActivity?)?.supportActionBar.let {
            it?.title = "Currency Price"//getString(R.string.product_app_bar_title)
            it?.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun setObserver() {

        viewModel.onGetAllCurrencies.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success -> {
                    hideInProgress()
                    val currenciesList = dataState.data
                    Log.d(TAG, "Success: "+currenciesList?.size)
                    listAdapter.setData(currenciesList as ArrayList<Currency>)
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

    private fun onClicked(): (Int) -> Unit = { _id ->
        findNavController().navigate(CurrenciesListFragmentDirections.actionCurrenciesListFragmentToCreateCurrencyFragment().setCurrencyId(_id))
    }

}