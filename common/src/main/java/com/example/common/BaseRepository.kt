package com.example.common

import com.example.common.error.ApiException
import com.example.common.error.NetWorkException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * ClassName:      BaseRepository
 * Description:    BaseRepository
 * Author:         mwy
 * CreateDate:     2020/6/20 20:00
 */
abstract class BaseRepository {
    /*suspend fun <T : Any> apiCall(call: suspend () -> WanResponse<T>): WanResponse<T> {
        return call.invoke()
    }*/

    protected suspend fun <T : Any> safeApiCall(
        errorMessage: String,
        call: suspend () -> ApiResult<T>
    ): ApiResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(NetWorkException(e.message ?: "网络异常", e))
        }
    }

    protected suspend fun <T : Any> executeResponse(
        response: BaseResponseResult<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): ApiResult<T> {
        return coroutineScope {
            if (!response.isSuccess()) {
                errorBlock?.let { it() }
                when {
                    response.isTokenERROR() -> {
                        ApiResult.Error(ApiException(response.getConvertErrorMsg()))
                    }
                    response.isNeedUpdate() -> {
                        ApiResult.Error(ApiException(response.getConvertErrorMsg()))
                    }
                    else -> {
                        ApiResult.Error(ApiException(response.getConvertErrorMsg()))
                    }
                }
            } else {
                successBlock?.let { it() }
                ApiResult.Success(response.getData())
            }
        }
    }
}