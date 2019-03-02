package com.pri.currencyconverter.ui.main

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.*
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import com.pri.currencyconverter.R
import com.pri.currencyconverter.api.model.Rate
import com.pri.currencyconverter.databinding.ActivityMainBinding
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    private val mapping = SparseArray<RadioButton>()

    private val compositeDisposable by lazy { CompositeDisposable() }

    @Inject
    lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding


    private var selectedRate: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainActivityViewModel

        mainActivityViewModel.setIsLoading(true)

        compositeDisposable.add(mainActivityViewModel.showDataFromApi()
                .subscribeBy(onSuccess = {
                    updateUIWithCurrencyList(it.rates)
                    mainActivityViewModel.setIsLoading(false)
                }, onError = {
                    /**/
                    Log.d("MainActivity", it.message)
                    mainActivityViewModel.setIsLoading(false)
                }))

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mainActivityViewModel.amountWithoutTax.addOnPropertyChangedCallback(
                object: Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(
                            observable: Observable?, i: Int) =
                            mainActivityViewModel.calculateResult(selectedRate)
                })
    }

    fun updateUIWithCurrencyList(currencies: List<Rate>) {
        val adapter = ArrayAdapter<Rate>(this, android.R.layout.simple_spinner_dropdown_item,
                currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.setAdapter(adapter)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val currencyModel = adapter.getItem(i)
                val periodModel = currencyModel.periods.get(0)
                updateUIWithRateList(periodModel.rates)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }

    fun updateUIWithRateList(rateModels: Map<String, Double>) {
        binding.rgTaxRate.removeAllViews()
        binding.rgTaxRate.setOnCheckedChangeListener(null)
        mapping.clear()
        binding.rgTaxRate.setOrientation(LinearLayout.VERTICAL)
        binding.rgTaxRate.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { rg, checkedId ->
            selectedRate = (mapping.get(checkedId) as RadioButton).tag as Double
            mainActivityViewModel.calculateResult(selectedRate)
        })
        var i = 0
        for (key in rateModels.keys) {
            val rbn = RadioButton(this)
            val radioButtonId = i + 1000
            rbn.id = radioButtonId
            rbn.setText(key + " - " + rateModels.get(key) + "%")
            rbn.tag = rateModels.get(key)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            rbn.layoutParams = params
            mapping.put(radioButtonId, rbn)
            if (i == 0) {
                rbn.isChecked = true
            }
            binding.rgTaxRate.addView(rbn)
            i++
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}
