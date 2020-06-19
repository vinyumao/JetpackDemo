package com.example.testbottomview.repository

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PageKeyedDataSource
import com.example.base.L
import com.example.bean.pixabay.Pixabay
import com.example.net.ApiService
import com.example.net.retrofit.service.PixabayService
import com.example.testbottomview.fragment.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ClassName:      PixabayDataSource
 * Description:    PixabayDataSource
 * Author:         mwy
 * CreateDate:     2020/6/19 18:16
 */
class PixabayDataSource(private val context: Context,private val dashboardViewModel: DashboardViewModel) : PageKeyedDataSource<Int, Pixabay.PhotoItem>() {
    private val queryKey = arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal").random()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Pixabay.PhotoItem>
    ) {
        L.i("PixabayDataSource CurrentThread:"+Thread.currentThread().name)
        /*var result = ApiService.getApiService<PixabayService>()
            .LoadPicture(queryKey, 1)

            callback.onResult(result.hits.toList(),null,2)*/
        dashboardViewModel.viewModelScope.launch {
            var result:Pixabay? = null
            withContext(Dispatchers.IO){
                result = ApiService.getApiService<PixabayService>()
                    .LoadPicture("17120440-b20cc91293cbce0d16b31fd2e",queryKey, 50,1)
            }
            result?.hits?.toMutableList()?.let {
                callback.onResult(it,null,2)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Pixabay.PhotoItem>
    ) {
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Pixabay.PhotoItem>
    ) {
    }
}