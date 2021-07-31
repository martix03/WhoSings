package it.marta.whosings.di

import it.marta.whosings.data.api.WhoSingsService
import org.koin.dsl.module
import retrofit2.Retrofit


val serviceModule = module {
    single { get<Retrofit>().create(WhoSingsService::class.java) }
}