package com.pri.currencyconverter.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.pri.currencyconverter.ui.main.MainActivity
import com.pri.currencyconverter.ui.main.di.MainActivityModule

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    abstract fun bindMainActivity(): MainActivity
}