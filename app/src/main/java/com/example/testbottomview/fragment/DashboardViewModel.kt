package com.example.testbottomview.fragment

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.common.BaseViewModel
import com.example.net.RequestStatus
import com.example.testbottomview.repository.PixabayDataSourceFactory
import com.example.testbottomview.repository.PixabayRepository

class DashboardViewModel @ViewModelInject constructor(
    application: Application,
    private var repository: PixabayRepository
) : BaseViewModel(application) {

    private val factory = PixabayDataSourceFactory(this)
    val pagedListLiveData = factory
        //.toLiveData(50)//如果是只有一个pageSize参数,看Config构造函数就可得知,默认第一次加载,会加载pageSize*3的数量
        //如果这里容上面的一个参数的 则需要在DataSource的loadInitial方法里调用CallBack时,nextPageKey需要+3,
        // 也就是要下一次加载时,需要加载的是第四页的数据
        .toLiveData(Config(100, prefetchDistance = 10, initialLoadSizeHint = 100))

    val requestStatus = Transformations.switchMap(factory.pixabayDataSource) { it.requestStatus }

    //刷新数据源
    fun reFreshData() {
        pagedListLiveData.value?.dataSource?.invalidate()
    }

    fun getRepository(): PixabayRepository {
        return repository
    }

    fun retryFetchData() {
        factory.pixabayDataSource.value?.retry?.invoke()
    }

}