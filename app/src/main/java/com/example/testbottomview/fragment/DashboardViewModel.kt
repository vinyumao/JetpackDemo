package com.example.testbottomview.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.testbottomview.repository.PixabayDataSourceFactory
import androidx.paging.toLiveData

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    val pagedListLiveData = PixabayDataSourceFactory(application,this).toLiveData(1)

}