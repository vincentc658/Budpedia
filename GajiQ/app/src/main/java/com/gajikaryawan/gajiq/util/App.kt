package com.gajikaryawan.gajiq.util

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(applicationContext)
    }
}