package com.example.base

import android.util.Log

/**
 * ClassName:      L
 * Description:    LogUtils
 * Author:         mwy
 * CreateDate:     2020/6/19 16:06
 */
object L {
    private const val TAG = "mwy"

    private val isDebug = BuildConfig.DEBUG

    fun v(msg: String?) = if(isDebug) Log.v(TAG, msg?:"") else 0

    fun d(msg: String?) = if(isDebug) Log.d(TAG, msg?:"") else 0

    fun i(msg: String?) = if(isDebug) Log.i(TAG, msg?:"") else 0

    fun w(msg: String?) = if(isDebug) Log.w(TAG, msg?:"") else 0

    fun e(msg: String?) = if(isDebug) Log.e(TAG, msg?:"") else 0

    fun forceV(msg: String?) = Log.v(TAG, msg?:"")

    fun forceD(msg: String?) = Log.d(TAG, msg?:"")

    fun forceI(msg: String?) = Log.i(TAG, msg?:"")

    fun forceW(msg: String?) = Log.w(TAG, msg?:"")

    fun forceE(msg: String?) = Log.e(TAG, msg?:"")
}