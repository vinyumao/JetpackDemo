package com.example.base.utlis

import android.content.Context

/**
 * @author cx
 */
object CacheUtils {
    /**
     * cache文件夹
     */
    fun getCacheDir(context: Context): String {
        return if (context.externalCacheDir != null && existSDCard()) {
            context.externalCacheDir!!.toString()
        } else {
            context.cacheDir.toString()
        }
    }

    /**
     * 是否有外部存储
     */
    private fun existSDCard(): Boolean {
        return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
    }
}