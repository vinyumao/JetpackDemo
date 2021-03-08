package com.example.testbottomview.fragment

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.common.BaseViewModel
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

    //Transformations.map:
    //容器A监听B内容的变化，变化时将B内容转化为相应的内容并通知A监听器
    //原理是利用MediatorLiveData的addSource对其他容器内容监听，在Observer中再对MediatorLiveData修改内容为转化了的相应的数据，
    // 来通知自己的监听器内容发生变化
    // 
    //Transformations.switchMap:
    //容器A监听B内容的变化，变化时从B内容获取相应的容器C，添加到A的监听列表里，即现在A同时监听B跟C，
    //而B内容的变化只会(switch)更换A监听列表里的C，C内容的变化才会通知A监听器
    //例子1：B为衣服品牌，C为衣服品牌对应的衣服价格，B与C一一对应关系，
    //那么效果就是A监听的是B对应的C(衣服品牌对应的价格)，衣服品牌变化为B2就监听变化了的品牌B2对应的价格C2，而不再监听之前的C1
    //例子2：业务上可能存在多个B对应一个C，比如B1,B2都是对应同一个C1，那么B1变成B2，A依然还是监听着C1
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