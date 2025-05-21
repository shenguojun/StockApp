package com.shengj.stockapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StockApplication : Application() {
    companion object {
        lateinit var instance: StockApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
} 