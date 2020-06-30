package com.example.testbottomview

import com.example.common.BaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: BaseApp() {
    companion object{
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}