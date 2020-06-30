package com.example.common

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.L
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * ClassName:      BaseVMActivity
 * Description:    BaseVMActivity
 * Author:         mwy
 * CreateDate:     2020/6/20 20:15
 */
abstract class BaseVMActivity<V : BaseViewModel> : BaseSimpleActivity() {


    protected fun initViewModelActions(viewModel:BaseViewModel) {
        viewModel.statusLiveData.observe(this, Observer { request ->
            request?.run {
                when (state) {
                    Request.State.START -> {//协程开始
                        L.d("coroutine开始")
                    }
                    Request.State.REFRESH -> {//协程开始&&进度菊花圈
                        if (showDialog) {
                            //DialogHelper.getInstance().showProgress(this@BaseActivity)
                        }
                        L.d("coroutine刷新")
                    }
                    Request.State.FINISH -> {//协程结束
                        if (showDialog) {
                            //DialogHelper.getInstance().dismissProgress()
                        }
                        L.d("coroutine结束")
                    }
                    Request.State.ERROR -> {//协程异常
                        if (showDialog) {
                            //DialogHelper.getInstance().dismissProgress()
                        }
                        L.d("coroutine异常:${exception}")
                    }
                }
            }
        })
    }

    fun launch(block: Block) {
        lifecycleScope.launch {
            try {
                block()
            } catch (e: Exception) {
                //异常处理
            }
        }
    }
}