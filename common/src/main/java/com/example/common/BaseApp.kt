package com.example.common

import android.app.Application

/**
 * ClassName:      BaseApp
 * Description:    BaseApplication
 * Author:         mwy
 * CreateDate:     2020/6/19 16:37
 */
open class BaseApp: Application() {
    companion object{
        public lateinit var instance: BaseApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}