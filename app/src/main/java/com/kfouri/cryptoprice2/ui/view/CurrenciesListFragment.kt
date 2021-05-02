package com.kfouri.cryptoprice2.ui.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.FragmentCurrenciesBinding
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
    private lateinit var binding: FragmentCurrenciesBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_currencies, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setObserver()
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        /*
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setFilterAdapter(newText)
                return true
            }
        })

         */
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                callNewCurrencyFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callNewCurrencyFragment() {
        findNavController().navigate(CurrenciesListFragmentDirections.actionCurrenciesListFragmentToCreateCurrencyFragment().setCurrencyId(-1))
    }
}