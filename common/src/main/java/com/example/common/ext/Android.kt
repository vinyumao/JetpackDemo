package com.example.common.ext

import android.accounts.NetworkErrorException
import androidx.lifecycle.ViewModel
import com.example.common.ApiResult
import com.example.common.BaseResponseResult
import com.example.common.error.ApiException
import com.example.common.error.NetWorkException
import kotlinx.coroutines.CoroutineScope

/**
 * ClassName:      Android
 * Description:    Android ext
 * Author:         mwy
 * CreateDate:     2020/6/23 21:28
 */
fun <T : Any> ViewModel.holdResult(
    result: ApiResult<T>,
    successBlock: ((data:T) -> Unit),
    apiErrorBlock: ((e:ApiException) -> Unit)? = null,
    netWorkErrorBlock: ((e:NetWorkException) -> Unit)? = null
) {
    if (result is ApiResult.Success){
        successBlock(result.data)
    }else if(result is ApiResult.Error){
        if (result.exception is ApiException){
            apiErrorBlock?.invoke(result.exception)
        }else{
            netWorkErrorBlock?.invoke(result.exception as NetWorkException)
        }
    }
}