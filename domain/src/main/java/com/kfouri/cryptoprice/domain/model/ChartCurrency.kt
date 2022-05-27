package com.kfouri.cryptoprice.domain.model

data class ChartCurrency(
    val prices: List<List<Double>>,
    val marketCaps: List<List<Double>>,
    val totalVolumes: List<List<Double>>
)