package it.marta.whosings

import android.app.Application
import it.marta.whosings.di.clientModule
import it.marta.whosings.di.repositoryModule
import it.marta.whosings.di.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WhoSingsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WhoSingsApplication)
            modules(clientModule, serviceModule, repositoryModule)
        }
    }
}