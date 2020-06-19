package com.example.testbottomview.repository

import android.content.Context
import androidx.paging.DataSource
import com.example.bean.pixabay.Pixabay
import com.example.testbottomview.fragment.DashboardViewModel

/**
 * ClassName:      PixbayDataSource
 * Description:    PixabayDataSourceFactory
 * Author:         mwy
 * CreateDate:     2020/6/19 18:42
 */
class PixabayDataSourceFactory(private val context: Context, private val dashboardViewModel: DashboardViewModel) : DataSource.Factory<Int,Pixabay.PhotoItem>(){
    override fun create(): DataSource<Int, Pixabay.PhotoItem> {
        return PixabayDataSource(context, dashboardViewModel)
    }
}