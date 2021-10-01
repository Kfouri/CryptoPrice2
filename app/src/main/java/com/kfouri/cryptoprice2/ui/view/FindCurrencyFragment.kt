package com.kfouri.cryptoprice2.ui.view

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kfouri.cryptoprice2.CurrencyJson
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.FragmentFindCurrenciesBinding
import com.kfouri.cryptoprice2.ext.setNavigationResult
import com.kfouri.cryptoprice2.ui.adapter.FindAdapter
import com.kfouri.cryptoprice2.ui.viewmodel.FindCurrenciesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class FindCurrencyFragment: Fragment()  {

    private lateinit var binding: FragmentFindCurrenciesBinding
    private val viewModel: FindCurrenciesViewModel by viewModels()
    private val findAdapter = FindAdapter(onClicked())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_currencies, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setHasOptionsMenu(true)

        binding.editTextSymbol.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setFilterAdapter(s)
            }
        })

        activity?.let {
            val jsonFileString = getJsonDataFromAsset(it.applicationContext, "currencies.json")
            val gson = Gson()
            val listCurrenciesType = object : TypeToken<List<CurrencyJson>>() {}.type
            val currencies: List<CurrencyJson> = gson.fromJson(jsonFileString, listCurrenciesType)
            findAdapter.setData(currencies)
        }

        setNavigationResult(null, "currency_id")

        binding.editTextSymbol.requestFocus()
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(binding.editTextSymbol, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setLayout() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = findAdapter
        }
    }

    private fun setFilterAdapter(newText: CharSequence?) {
        findAdapter.filter.filter(newText)
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun onClicked(): (String) -> Unit = { id ->
        setNavigationResult(id, "currency_id")
        activity?.onBackPressed()
    }
}