package com.example.base.utlis

import android.content.Context
import android.util.TypedValue

/**
 * ClassName:      DensityUtil
 * Description:    DensityUtil
 * Author:         mwy
 * CreateDate:     2020/6/19 21:05
 */
object DensityUtil {
    fun dp2px(context: Context, dpVal: Float): Int {
        return (dpVal * context.getResources().getDisplayMetrics().density + 0.5f).toInt()
    }
}