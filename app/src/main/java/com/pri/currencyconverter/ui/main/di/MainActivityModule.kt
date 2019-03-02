package com.pri.currencyconverter.ui.main.di

import dagger.Module
import dagger.Provides
import com.pri.currencyconverter.repository.Repository
import com.pri.currencyconverter.ui.main.MainActivityViewModel
import com.pri.currencyconverter.util.SchedulerProvider

@Module
class MainActivityModule {

    @Provides
    fun provideViewModel(repository: Repository, schedulerProvider: SchedulerProvider) = MainActivityViewModel(repository, schedulerProvider)
}