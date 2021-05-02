package com.kfouri.cryptoprice2.ui.view

import android.os.Bundle
import android.view.*
import android.view.View.OnFocusChangeListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.FragmentCreateCurrencyBinding
import com.kfouri.cryptoprice2.ext.showError
import com.kfouri.cryptoprice2.ext.showSnackbar
import com.kfouri.cryptoprice2.ui.viewmodel.CreateCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_currency.*

@AndroidEntryPoint
class CreateCurrencyFragment: Fragment() {

    private val viewModel: CreateCurrencyViewModel by viewModels()
    private var currencyId = -1
    private lateinit var binding: FragmentCreateCurrencyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_currency,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyId = arguments?.getInt("currencyId")?: -1

        setObserver()

        if (currencyId == -1) {
            (activity as MainActivity).supportActionBar?.title = "New Currency"
            setHasOptionsMenu(false)
        } else {
            (activity as MainActivity).supportActionBar?.title = "Edit Currency"
            setHasOptionsMenu(true)
            viewModel.getCurrencyById(currencyId)
        }

        buttonCancel.setOnClickListener {
            activity?.onBackPressed()
        }

        editText_symbol.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && !editText_symbol.text.isNullOrBlank()) {
                viewModel.getNetworkCurrency(editText_symbol.text.toString())
            }
        }
    }

    private fun setObserver() {

        viewModel.onFinish.observe(viewLifecycleOwner, {
            activity?.onBackPressed()
        })

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove -> {
                removeCurrency()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun removeCurrency() {

        //AGREGAR DIALOGO PARA CONFIRMAR SI DESEA ELIMINAR LA MONEDA
        viewModel.removeCurrency()
    }
}