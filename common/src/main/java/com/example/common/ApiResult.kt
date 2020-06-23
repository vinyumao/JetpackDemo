package com.example.common

/**
 * ClassName:      Result
 * Description:    Result
 * Author:         mwy
 * CreateDate:     2020/6/23 19:21
 */
 sealed class ApiResult<out T:Any> {
    data class Success<T : Any>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception) : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}