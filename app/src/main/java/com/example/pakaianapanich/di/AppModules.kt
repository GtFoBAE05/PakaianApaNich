package com.example.pakaianapanich.di

import com.example.pakaianapanich.data.UserRepository
import com.example.pakaianapanich.data.source.remote.api.ApiConfig
import com.example.pakaianapanich.ui.homescreen.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { ApiConfig().getApiService() }
}

val repositoryModule = module {
    single { UserRepository(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}