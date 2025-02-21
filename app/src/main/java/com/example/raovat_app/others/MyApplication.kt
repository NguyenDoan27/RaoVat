package com.example.raovat_app.others

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


val preferenceModule = module {
    single {PreferenceDataStore(get())}
}
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(preferenceModule)
        }
    }
}