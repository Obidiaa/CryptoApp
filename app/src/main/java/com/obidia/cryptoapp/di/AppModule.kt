package com.obidia.cryptoapp.di

import com.obidia.cryptoapp.core.data.networking.HttpClientFactory
import com.obidia.cryptoapp.crypto.data.networking.RemoteCryptoDataSource
import com.obidia.cryptoapp.crypto.domain.CryptoDataSource
import com.obidia.cryptoapp.crypto.presentation.cryptodetail.CryptoDetailViewModel
import com.obidia.cryptoapp.crypto.presentation.cryptolist.CryptoListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCryptoDataSource).bind<CryptoDataSource>()

    viewModel { CryptoListViewModel(get()) }
    viewModel { CryptoDetailViewModel(get()) }
}