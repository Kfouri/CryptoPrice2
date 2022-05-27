package com.kfouri.cryptoprice.ui.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kfouri.cryptoprice.R
import com.kfouri.cryptoprice.databinding.FragmentCreateCurrencyBinding
import com.kfouri.cryptoprice.ext.getNavigationResultLiveData
import com.kfouri.cryptoprice.ext.toEditable
import com.kfouri.cryptoprice.ui.viewmodel.CreateCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCurrencyFragment: Fragment() {

    private val viewModel: CreateCurrencyViewModel by viewModels()
    private lateinit var binding: FragmentCreateCurrencyBinding
    private lateinit var saveButton: MenuItem
    private var localCount = 0

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
        setHasOptionsMenu(true)
        localCount = arguments?.getInt("count") ?: 0

        (activity as MainActivity).supportActionBar?.title = "New Currency"
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.searchButton.setOnClickListener {
            findNavController().navigate(CreateCurrencyFragmentDirections.actionCreateCurrencyFragmentToFindCurrencyFragment())
        }

        val result = getNavigationResultLiveData<String>("currency_id")
        result?.observe(viewLifecycleOwner) { res ->
            res?.let {
                val listResult = it.split("#|#")

                viewModel.currencyId = listResult[0]
                viewModel.currencyName = listResult[1]
                viewModel.currencySymbol = listResult[2]

                binding.editTextSymbol.text = viewModel.currencySymbol.toEditable()
                viewModel.getNetworkCurrency()
            }
        }
        setObserver()
    }

    private fun setObserver() {

        viewModel.onFinish.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }

        viewModel.buttonSaveStatusLiveData.observe(viewLifecycleOwner) { status ->
            if (this::saveButton.isInitialized) {
                setStatusSaveButton(status)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        menu.getItem(1).isVisible = false
        saveButton = menu.getItem(0)
        setStatusSaveButton(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove -> {
                removeCurrency()
                true
            }
            R.id.action_save -> {
                viewModel.saveCurrencyClick(localCount)
                true
            }
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun removeCurrency() {

        //AGREGAR DIALOGO PARA CONFIRMAR SI DESEA ELIMINAR LA MONEDA
        viewModel.removeCurrency()
    }

    private fun setStatusSaveButton(status: Boolean) {
        if (status) {
            saveButton.isEnabled = true
            saveButton.icon = requireActivity().getDrawable(R.drawable.ic_save)
        } else {
            saveButton.isEnabled = false
            saveButton.icon = requireActivity().getDrawable(R.drawable.ic_save_disable)
        }
    }
}