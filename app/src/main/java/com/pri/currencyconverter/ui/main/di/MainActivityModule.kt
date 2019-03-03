package com.pri.currencyconverter.ui.main.di

import android.arch.lifecycle.ViewModelProvider
import com.pri.currencyconverter.di.ViewModelProviderFactory
import com.pri.currencyconverter.repository.Repository
import com.pri.currencyconverter.ui.main.MainActivityViewModel
import com.pri.currencyconverter.util.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun ViewModel(repository: Repository, schedulerProvider: SchedulerProvider) = MainActivityViewModel(repository, schedulerProvider)

    @Provides
    internal fun provideViewModel(viewModel: MainActivityViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }
}