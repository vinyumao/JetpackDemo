package com.example.testbottomview.repository

import android.app.Application
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.common.BaseRepository
import com.example.testbottomview.fragment.DashboardViewModel

/**
 * ClassName:      PixabayRepository
 * Description:    PixabayRepository
 * Author:         mwy
 * CreateDate:     2020/6/20 20:52
 */
class PixabayRepository(private val application: Application, private val dashboardViewModel: DashboardViewModel): BaseRepository() {
    val pagedListLiveData = PixabayDataSourceFactory(
        application,
        dashboardViewModel)
        //.toLiveData(50)//如果是只有一个pageSize参数,看Config构造函数就可得知,默认第一次加载,会加载pageSize*3的数量
        //如果这里容上面的一个参数的 则需要在DataSource的loadInitial方法里调用CallBack时,nextPageKey需要+3,
        // 也就是要下一次加载时,需要加载的是第四页的数据
        .toLiveData(Config(50,prefetchDistance = 10,initialLoadSizeHint = 50))
    //刷新数据源
    fun resetData(){
        pagedListLiveData.value?.dataSource?.invalidate()
    }
}