package com.pri.currencyconverter.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.SparseArray
import android.view.View
import android.widget.*
import com.pri.currencyconverter.R
import com.pri.currencyconverter.api.model.Rate
import com.pri.currencyconverter.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var mainActivityViewModel: MainActivityViewModel? = null
    private lateinit var binding: ActivityMainBinding

    private val mapping = SparseArray<RadioButton>()
    private var selectedRate: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainActivityViewModel::class.java)

        binding.viewModel = mainActivityViewModel
        binding.lifecycleOwner = this

        mainActivityViewModel!!.setIsLoading(true)

        mainActivityViewModel!!.rateList.observe(this@MainActivity, Observer {
            it?.let { it1 -> updateUIWithCurrencyList(it1) }
        })

        mainActivityViewModel!!.amountWithoutTax.observe(this@MainActivity, Observer {
            it?.let {
                mainActivityViewModel!!.calculateResult(selectedRate)
            }
        })
        mainActivityViewModel!!.erroMsg.observe(this@MainActivity, Observer {
            if(!it.isNullOrBlank()) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
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
            mainActivityViewModel!!.calculateResult(selectedRate)
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


}
