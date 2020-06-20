package com.example.testbottomview.fragment

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.base.L
import com.example.common.BaseVMFragment
import com.example.testbottomview.R
import com.example.testbottomview.repository.PixabayRepository
import kotlinx.android.synthetic.main.dashboard_fragment.*

class DashboardFragment : BaseVMFragment<PixabayRepository,DashboardViewModel>() {

    override fun getLayoutResID() = R.layout.dashboard_fragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        L.i( "onActivityCreated: ")
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        val dashboardAdapter = DashboardAdapter()
        mRvPicture.apply {
            adapter = dashboardAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dashboardAdapter.submitList(it)
            mSwipeRefreshLayout.isRefreshing = false
        })
        mSwipeRefreshLayout.setOnRefreshListener {
            viewModel.reFreshData()
        }
        mSwipeRefreshLayout.isRefreshing = true
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        L.i( "onAttach: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        L.i( "onViewCreated: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        L.i( "onDestroy: ")
    }

    override fun createViewModel() = ViewModelProvider(this).get(DashboardViewModel::class.java)
}