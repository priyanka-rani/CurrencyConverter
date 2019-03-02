package com.pri.currencyconverter.api.model

/**
 * Created by Priyanka on 27/2/19.
 */
data class CurrencyModel(
        val details: String,
        val rates: List<Rate>,
        val version: Any
)