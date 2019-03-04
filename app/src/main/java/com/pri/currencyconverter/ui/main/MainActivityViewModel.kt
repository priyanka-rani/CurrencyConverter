package com.pri.currencyconverter.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import android.util.Log
import com.pri.currencyconverter.api.model.Rate
import com.pri.currencyconverter.repository.Repository
import com.pri.currencyconverter.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy


class MainActivityViewModel(private val repository: Repository, private val schedulerProvider: SchedulerProvider): ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    val amountWithTax = MutableLiveData<String>()

    val amountWithoutTax = MutableLiveData<String>()

    val erroMsg = MutableLiveData<String>()

    val rateList = MutableLiveData<List<Rate>>()

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
       showDataFromApi()
    }
    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun showDataFromApi(){
        setIsLoading(true)
        compositeDisposable.add(repository.getDataFromApi()
                .compose(schedulerProvider.getSchedulersForSingle())
                .subscribeBy (onSuccess = {
                    rateList.value = it.rates
                   setIsLoading(false)
                }, onError = {
                    /**/
                    Log.d("MainActivity", it.message)
                    erroMsg.value = it.message
                   setIsLoading(false)
                }))
    }

    fun calculateResult(value: Double?) {
        if (!TextUtils.isEmpty(amountWithoutTax.value) && value != null) {
            val amountInput = java.lang.Double.parseDouble(amountWithoutTax.value)
            val taxRate = value
            val result = amountInput + amountInput * taxRate / 100
            amountWithTax.value = result.toString()
        } else {
            amountWithTax.value = ""
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
