package com.pri.currencyconverter.ui.main

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.TextUtils
import io.reactivex.Single
import com.pri.currencyconverter.api.model.CurrencyModel
import com.pri.currencyconverter.repository.Repository
import com.pri.currencyconverter.util.SchedulerProvider


class MainActivityViewModel(private val repository: Repository, private val schedulerProvider: SchedulerProvider) {

    val isLoading = ObservableBoolean(false)

    val amountWithTax = ObservableField<String>("")

    val amountWithoutTax = ObservableField<String>("")


    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }

    fun showDataFromApi(): Single<CurrencyModel> = repository.getDataFromApi()
            .compose(schedulerProvider.getSchedulersForSingle())

    fun calculateResult(value: Double?) {
        if (!TextUtils.isEmpty(amountWithoutTax.get()) && value != null) {
            val ammountInput = java.lang.Double.parseDouble(amountWithoutTax.get())
            val taxRate = value
            val result = ammountInput + ammountInput * taxRate / 100
            amountWithTax.set(result.toString())
        } else {
            amountWithTax.set("")
        }
    }


}
