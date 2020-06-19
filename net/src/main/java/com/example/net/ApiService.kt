package com.example.net

import com.example.net.retrofit.Api

/**
 * ClassName:      ApiService
 * Description:    ApiService工具类
 * Author:         mwy
 * CreateDate:     2020/6/19 17:29
 */
object ApiService {
    private val map = mutableMapOf<Class<*>, Any?>()

    inline fun <reified T> getApiService() =
        getService(T::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <T> getService(type: Class<T>): T {
        return if (map.containsKey(type)) {
            map[type] as T
        } else {
            val temp = Api.instance.retrofit.create(type)
            map[type] = temp
            temp
        }
    }
}