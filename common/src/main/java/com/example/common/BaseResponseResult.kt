package com.example.common

/**
 * ClassName:      BaseResult
 * Description:    BaseResult
 * Author:         mwy
 * CreateDate:     2020/6/23 18:15
 */
interface BaseResponseResult<T : Any> {
    //请求是否成功
    fun isSuccess(): Boolean

    //token错误,失效 需要重新登录
    fun isTokenERROR(): Boolean

    //需要更新app
    fun isNeedUpdate(): Boolean

    fun getConvertErrorMsg(): String

    fun getData(): T
}