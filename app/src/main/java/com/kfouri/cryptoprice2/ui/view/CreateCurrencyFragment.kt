package com.kfouri.cryptoprice2.ui.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.FragmentCreateCurrencyBinding
import com.kfouri.cryptoprice2.ext.getNavigationResultLiveData
import com.kfouri.cryptoprice2.ext.toEditable
import com.kfouri.cryptoprice2.ui.viewmodel.CreateCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCurrencyFragment: Fragment() {

    private val viewModel: CreateCurrencyViewModel by viewModels()
    private var localCurrencyId = -1
    private lateinit var binding: FragmentCreateCurrencyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        localCurrencyId = arguments?.getInt("currencyId") ?: -1

        setObserver()

        if (localCurrencyId == -1) {
            (activity as MainActivity).supportActionBar?.title = "New Currency"
            setHasOptionsMenu(false)
        } else {
            (activity as MainActivity).supportActionBar?.title = "Edit Currency"
            setHasOptionsMenu(true)
            viewModel.getCurrencyById(localCurrencyId)
        }

        binding.buttonCancel.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.searchButton.setOnClickListener {
            findNavController().navigate(CreateCurrencyFragmentDirections.actionCreateCurrencyFragmentToFindCurrencyFragment())
        }

        val result = getNavigationResultLiveData<String>("currency_id")
        result?.observe(viewLifecycleOwner) { result ->
            result?.let {
                val listResult = result.toString().split("#|#")

                viewModel.currencyId = listResult[0]
                viewModel.currencyName = listResult[1]
                viewModel.currencySymbol = listResult[2]

                binding.editTextSymbol.text = viewModel.currencySymbol.toEditable()
                viewModel.getNetworkCurrency()
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