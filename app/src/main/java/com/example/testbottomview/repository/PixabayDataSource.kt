package com.example.testbottomview.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.base.L
import com.example.common.bean.Pixabay
import com.example.common.ext.holdResult
import com.example.net.RequestStatus
import com.example.testbottomview.fragment.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * ClassName:      PixabayDataSource
 * Description:    PixabayDataSource
 * Author:         mwy
 * CreateDate:     2020/6/19 18:16
 */
class PixabayDataSource(private val dashboardViewModel: DashboardViewModel) :
    PageKeyedDataSource<Int, Pixabay.PhotoItem>() {
    var retry : (()->Any)? = null
    private val queryKey =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal").random()

    private val _requestStats = MutableLiveData<RequestStatus>()
    var requestStatus:LiveData<RequestStatus> = _requestStats

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Pixabay.PhotoItem>
    ) {
        retry = null
        L.i("requestedLoadSize:${params.requestedLoadSize}")
        _requestStats.postValue(RequestStatus.INITIAL_LOADING)
        dashboardViewModel.launch {
            withContext(Dispatchers.IO) {
                val result = dashboardViewModel.getRepository()
                    .loadPicture(queryKey, params.requestedLoadSize, 1)
                dashboardViewModel.holdResult(result,{
                    it.hits.toMutableList().let { dataList ->
                        callback.onResult(dataList, null, 2)
                        _requestStats.postValue(RequestStatus.LOADED)
                    }
                },{
                    retry = {loadInitial(params,callback)}
                    _requestStats.postValue(RequestStatus.FAILED)
                    L.e("ApiResult.Error:${result.toString()}")
                },{
                    retry = {loadInitial(params,callback)}
                    _requestStats.postValue(RequestStatus.FAILED)
                    L.e("ApiResult.Error:${result.toString()}-----${it.message}")
                })
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Pixabay.PhotoItem>
    ) {
        retry = null
        _requestStats.postValue(RequestStatus.LOADING)
        L.i("requestedLoadSize:${params.requestedLoadSize}")
        dashboardViewModel.launch(refresh = false) {
            withContext(Dispatchers.IO) {
                val result = dashboardViewModel.getRepository()
                    .loadPicture(queryKey, params.requestedLoadSize, params.key)

                dashboardViewModel.holdResult(result,{
                    it.hits.toMutableList().let {dataList ->
                        callback.onResult(dataList, params.key + 1)
                        _requestStats.postValue(RequestStatus.LOADED)
                    }
                },{
                    retry = {loadAfter(params,callback)}
                    L.e("ApiResult.Error:${result.toString()}")
                },{
                    L.e("ApiResult.Error:${result.toString()}")
                    if (it.originalException is HttpException && it.message == "HTTP 400 "){
                        L.e("RequestStatus.COMPLETED")
                        _requestStats.postValue(RequestStatus.COMPLETED)
                    }else{
                        L.e("not HttpException")
                        retry = {loadAfter(params,callback)}
                        _requestStats.postValue(RequestStatus.FAILED)
                    }
                })
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Pixabay.PhotoItem>
    ) {
    }
}