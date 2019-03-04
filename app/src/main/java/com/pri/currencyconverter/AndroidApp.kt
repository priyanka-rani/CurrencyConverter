package com.pri.currencyconverter

import com.pri.currencyconverter.di.DaggerAppComponent
import dagger.android.support.DaggerApplication

class AndroidApp : DaggerApplication() {
    private val applicationInjector = DaggerAppComponent.builder()
            .application(this)
            .build()

    override fun applicationInjector() = applicationInjector
}