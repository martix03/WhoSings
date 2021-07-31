package it.marta.whosings.di

import it.marta.whosings.data.repository.WhoSingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module


val repositoryModule = module {
    single { (dispatcher: CoroutineDispatcher) ->
        WhoSingsRepository(dispatcher)
    }
}