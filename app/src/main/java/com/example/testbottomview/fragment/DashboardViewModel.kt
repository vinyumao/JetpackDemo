package com.example.testbottomview.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import com.example.testbottomview.repository.PixabayDataSourceFactory
import androidx.paging.toLiveData
import com.example.bean.pixabay.Pixabay
import com.example.common.BaseViewModel
import com.example.testbottomview.repository.PixabayRepository

class DashboardViewModel(application: Application) : BaseViewModel<PixabayRepository>(application) {
    override fun createRepository() = PixabayRepository(getApplication(),this)
    fun getLiveData():LiveData<PagedList<Pixabay.PhotoItem>>{
        return repository.pagedListLiveData
    }

    fun reFreshData(){
        repository.resetData()
    }
}