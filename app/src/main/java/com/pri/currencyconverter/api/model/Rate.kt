package com.pri.currencyconverter.api.model

/**
 * Created by Priyanka on 27/2/19.
 */
data class Rate(
        val name: String, var periods: List<Period> = ArrayList(), var country_code: String = "", var code: String = "") {
    override fun toString(): String = name
}