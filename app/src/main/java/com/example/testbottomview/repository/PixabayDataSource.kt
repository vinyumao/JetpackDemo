package com.example.testbottomview.repository

import androidx.paging.PageKeyedDataSource
import com.example.base.L
import com.example.common.ApiResult
import com.example.common.bean.Pixabay
import com.example.common.ext.holdResult
import com.example.testbottomview.fragment.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ClassName:      PixabayDataSource
 * Description:    PixabayDataSource
 * Author:         mwy
 * CreateDate:     2020/6/19 18:16
 */
class PixabayDataSource(private val dashboardViewModel: DashboardViewModel) :
    PageKeyedDataSource<Int, Pixabay.PhotoItem>() {
    private val queryKey =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal").random()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Pixabay.PhotoItem>
    ) {
        L.i("requestedLoadSize:${params.requestedLoadSize}")
        dashboardViewModel.launch {
            withContext(Dispatchers.IO) {
                val result = dashboardViewModel.getRepository()
                    .LoadPicture(queryKey, params.requestedLoadSize, 1)
                dashboardViewModel.holdResult(result,{
                    it.hits.toMutableList().let { dataList ->
                        callback.onResult(dataList, null, 2)
                    }
                },{
                    L.e("ApiResult.Error:${result.toString()}")
                },{
                    L.e("ApiResult.Error:${result.toString()}")
                })
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Pixabay.PhotoItem>
    ) {
        L.i("requestedLoadSize:${params.requestedLoadSize}")
        dashboardViewModel.launch(refresh = false) {
            withContext(Dispatchers.IO) {
                val result = dashboardViewModel.getRepository()
                    .LoadPicture(queryKey, params.requestedLoadSize, 1)

                dashboardViewModel.holdResult(result,{
                    it.hits.toMutableList().let {dataList ->
                        callback.onResult(dataList, params.key + 1)
                    }
                },{
                    L.e("ApiResult.Error:${result.toString()}")
                },{
                    L.e("ApiResult.Error:${result.toString()}")
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