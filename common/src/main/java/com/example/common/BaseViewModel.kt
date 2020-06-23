package com.example.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ClassName:      BaseViewModel
 * Description:    BaseViewModel
 * Author:         mwy
 * CreateDate:     2020/6/20 20:01
 */
abstract class BaseViewModel<T:BaseRepository>(application: Application) : AndroidViewModel(application) {
    protected val repository by lazy {
        createRepository()
    }

    abstract fun createRepository(): T

    /**
     * 协程状态管理
     *
     * 开始 CoroutineState.State.START
     * 刷新 CoroutineState.State.REFRESH
     * 结束 CoroutineState.State.FINISH
     * 异常 CoroutineState.State.ERROR
     */
    val statusLiveData: MutableLiveData<Request> by lazy {
        MutableLiveData<Request>()
    }
    fun launch(refresh: Boolean = true,showDialog: Boolean = false, block: Block) {
        viewModelScope.launch {
            try {
                if (refresh) {
                    statusLiveData.postValue(Request(Request.State.REFRESH,showDialog))
                } else {
                    statusLiveData.postValue(Request(Request.State.START,showDialog))
                }
                block()
                statusLiveData.postValue(Request(Request.State.FINISH,showDialog))
            } catch (e: Exception) {
                statusLiveData.postValue(Request(Request.State.ERROR,showDialog,e))
                //处理协程异常
            }
        }
    }
}