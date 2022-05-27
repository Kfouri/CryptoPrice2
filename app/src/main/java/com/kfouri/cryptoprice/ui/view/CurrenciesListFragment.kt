package com.kfouri.cryptoprice.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.kfouri.cryptoprice.R
import com.kfouri.cryptoprice.databinding.FragmentCurrenciesBinding
import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.domain.state.DataState
import com.kfouri.cryptoprice.ext.hideInProgress
import com.kfouri.cryptoprice.ext.showError
import com.kfouri.cryptoprice.ext.showInProgress
import com.kfouri.cryptoprice.ui.adapter.ListAdapter
import com.kfouri.cryptoprice.ui.view.authentication.CreatePasswordFragment
import com.kfouri.cryptoprice.ui.viewmodel.CurrenciesListViewModel
import dagger.hilt.android.AndroidEntryPoint

private val TAG = CurrenciesListFragment::class.java.simpleName

@AndroidEntryPoint
class CurrenciesListFragment: Fragment() {

    private val viewModel: CurrenciesListViewModel by viewModels()
    private val listAdapter = ListAdapter(onClicked(), onRefreshList())
    private lateinit var binding: FragmentCurrenciesBinding
    private var searchView : SearchView? = null
    private var isBalanceShowed: Boolean = true
    private lateinit var preferences: SharedPreferences
    private val PREF_EYE_OPEN = "pref_eye_open"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
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

        val adRequest = AdRequest.Builder().build()

        binding.adView.loadAd(adRequest)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.getAllCurrencies()
        }
        activity?.let {
            preferences = it.getSharedPreferences(CreatePasswordFragment.PREF, Context.MODE_PRIVATE)
            isBalanceShowed = !preferences.getBoolean(PREF_EYE_OPEN, true)
            setEyeStatus()
        }

        binding.imageViewEye.setOnClickListener {
            setEyeStatus()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }

        binding.buttonEmptyList.setOnClickListener {
            callNewCurrencyFragment()
        }
    }

    private fun setEyeStatus() {
        isBalanceShowed = !isBalanceShowed
        if (isBalanceShowed) {
            binding.imageViewEye.setImageResource(R.drawable.ic_eye_open)
            binding.textViewBalance.visibility = View.VISIBLE
            binding.textViewBalanceHide.visibility = View.GONE
        } else {
            binding.imageViewEye.setImageResource(R.drawable.ic_eye_closed)
            binding.textViewBalance.visibility = View.GONE
            binding.textViewBalanceHide.visibility = View.VISIBLE
        }

        listAdapter.setBalanceVisibility(isBalanceShowed)

        val editor = preferences.edit()
        editor.putBoolean(PREF_EYE_OPEN, isBalanceShowed)
        editor.apply()
    }

    private fun setLayout() {
        setAppBar()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL))
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
                    if (currenciesList?.size!! > 0) {
                        binding.groupEmptyList.visibility = View.GONE
                        binding.groupList.visibility = View.VISIBLE
                    } else {
                        binding.groupEmptyList.visibility = View.VISIBLE
                        binding.groupList.visibility = View.GONE
                    }
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
        //findNavController().navigate(CurrenciesListFragmentDirections.actionCurrenciesListFragmentToCreateCurrencyFragment().setCurrencyId(_id))
        findNavController().navigate(CurrenciesListFragmentDirections.actionEditCurrency().setCurrencyId(_id))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.finish()
                return true
            }
            R.id.action_add -> {
                callNewCurrencyFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callNewCurrencyFragment() {
        findNavController().navigate(CurrenciesListFragmentDirections.actionCurrenciesListFragmentToCreateCurrencyFragment().setCount(listAdapter.itemCount))
    }

    private fun setFilterAdapter(newText: String?) {
        listAdapter.filter.filter(newText)
    }

    private fun onRefreshList() : (Double, Double) -> Unit = { totalInvested, balance ->
        viewModel.refreshHeader(totalInvested, balance)
    }
}