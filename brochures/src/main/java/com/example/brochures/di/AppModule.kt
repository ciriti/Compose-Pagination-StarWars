package com.example.brochures.di

import com.example.brochures.data.datasource.remote.BrochureApi
import com.example.brochures.domain.datasource.remote.BrochureDataSource
import com.example.brochures.data.datasource.remote.BrochureDataSourceImpl
import com.example.brochures.data.datasource.remote.BrochureResponse
import com.example.brochures.data.datasource.remote.BrochureResponseDeserializer
import com.example.brochures.domain.datasource.repository.BrochureRepository
import com.example.brochures.data.repository.BrochureRepositoryImpl
import com.example.brochures.ui.screen.brochure.BrochureViewModel
import com.google.gson.GsonBuilder
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

val appModule = module {

    single<URL> { URL("https://mobile-s3-test-assets.aws-sdlc-bonial.com/") }

    single<BrochureApi> {

        val gson = GsonBuilder()
            .registerTypeAdapter(BrochureResponse::class.java, BrochureResponseDeserializer())
            .create()

        Retrofit.Builder()
            .baseUrl(get<URL>())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BrochureApi::class.java)
    }

    single<BrochureDataSource> { BrochureDataSourceImpl(get()) }

    single<BrochureRepository> { BrochureRepositoryImpl(get()) }

    viewModel { BrochureViewModel(get()) }
}
