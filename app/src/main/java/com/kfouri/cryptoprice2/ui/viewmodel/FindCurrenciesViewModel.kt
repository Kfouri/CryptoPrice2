package com.kfouri.cryptoprice2.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindCurrenciesViewModel
@Inject
constructor(
): ViewModel() {

    val searchObservable = ObservableField<String>()
}