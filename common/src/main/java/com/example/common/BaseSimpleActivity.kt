package com.example.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * ClassName:      BaseSimpleActivity
 * Description:    BaseSimpleActivity
 * Author:         mwy
 * CreateDate:     2020/6/20 20:18
 */
abstract class BaseSimpleActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        initViewModel()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }


    open fun initViewModel() {

    }

    abstract fun getContentView(): Int

    abstract fun initData(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)

}