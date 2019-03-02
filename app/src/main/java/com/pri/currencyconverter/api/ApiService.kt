package com.pri.currencyconverter.api

import io.reactivex.Single
import retrofit2.http.GET
import com.pri.currencyconverter.api.model.CurrencyModel

interface ApiService {

    @GET(".")
    fun getJsonResponse(): Single<CurrencyModel>
}