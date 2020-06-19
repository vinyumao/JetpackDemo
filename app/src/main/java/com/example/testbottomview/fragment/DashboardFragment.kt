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
import com.example.testbottomview.R
import kotlinx.android.synthetic.main.dashboard_fragment.*

class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        L.i( "onCreateView: ")
        return inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        L.i( "onActivityCreated: ")
        val dashboardAdapter = DashboardAdapter()
        mRvPictrue.apply {
            adapter = dashboardAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
        viewModel.pagedListLiveData.observe(viewLifecycleOwner, Observer {
            dashboardAdapter.submitList(it)
        })
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