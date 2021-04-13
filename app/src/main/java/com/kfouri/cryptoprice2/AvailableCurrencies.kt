package com.kfouri.cryptoprice2

import com.kfouri.cryptoprice2.domain.model.CurrencyAvailableNetwork

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