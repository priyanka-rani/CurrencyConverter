package com.pri.currencyconverter.api.model

/**
 * Created by Priyanka on 27/2/19.
 */
data class Period(
        val effective_from: String,
        val rates: Map<String, Double>
)