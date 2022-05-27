package com.kfouri.cryptoprice

import com.kfouri.cryptoprice.domain.model.CurrencyAvailableNetwork

object AvailableCurrencies {

    private var availableList = ArrayList<CurrencyAvailableNetwork>()

    @JvmName("setAvailableListJava")
    fun setAvailableList(list: ArrayList<CurrencyAvailableNetwork>) {
        availableList = list
    }

    fun getAvailableList(): ArrayList<CurrencyAvailableNetwork> {
        return availableList
    }

    fun getSize(): Int = availableList.size

}