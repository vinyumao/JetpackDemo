package com.example.testbottomview

import android.app.Application
import com.example.common.BaseApp

class MyApp: BaseApp() {
    companion object{
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}