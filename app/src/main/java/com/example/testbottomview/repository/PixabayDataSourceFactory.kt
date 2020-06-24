package com.example.testbottomview.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.common.bean.Pixabay
import com.example.testbottomview.fragment.DashboardViewModel

/**
 * ClassName:      PixbayDataSource
 * Description:    PixabayDataSourceFactory
 * Author:         mwy
 * CreateDate:     2020/6/19 18:42
 */
class PixabayDataSourceFactory(private val dashboardViewModel: DashboardViewModel) : DataSource.Factory<Int, Pixabay.PhotoItem>(){
    private val _pixabayDataSource = MutableLiveData<PixabayDataSource>()
    val pixabayDataSource : LiveData<PixabayDataSource> = _pixabayDataSource
    override fun create(): DataSource<Int, Pixabay.PhotoItem> {
        return PixabayDataSource(dashboardViewModel).also { _pixabayDataSource.postValue(it) }
    }
}