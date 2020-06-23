package com.example.testbottomview.repository

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
    override fun create(): DataSource<Int, Pixabay.PhotoItem> {
        return PixabayDataSource(dashboardViewModel)
    }
}