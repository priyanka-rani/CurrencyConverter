package com.pri.currencyconverter.repository

import com.pri.currencyconverter.api.ApiService
import com.pri.currencyconverter.api.model.CurrencyModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val apiService: ApiService) {

    fun getDataFromApi(): Single<CurrencyModel> = apiService.getJsonResponse()

}