package com.android.bishkekroutes.application

import android.app.Application
import com.android.bishkekroutes.DatabaseRepository

class BishkekRoutesApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initialize()
    }
}