package com.example.testbottomview.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.base.L
import com.example.common.BaseVMFragment
import com.example.net.RequestStatus
import com.example.testbottomview.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard_fragment.*

@AndroidEntryPoint
class DashboardFragment : BaseVMFragment() {

    private val viewModel by viewModels<DashboardViewModel>()

    override fun getLayoutResID() = R.layout.dashboard_fragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        L.i( "onActivityCreated: ")
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        val dashboardAdapter = DashboardAdapter(viewModel)
        mRvPicture.apply {
            adapter = dashboardAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
        viewModel.pagedListLiveData.observe(viewLifecycleOwner, Observer {
            dashboardAdapter.submitList(it)
        })
        viewModel.requestStatus.observe(viewLifecycleOwner, Observer {
            dashboardAdapter.updateRequestStatus(it)
            mSwipeRefreshLayout.isRefreshing = it == RequestStatus.INITIAL_LOADING
            L.i("RequestStatus:${it.name}")
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
}