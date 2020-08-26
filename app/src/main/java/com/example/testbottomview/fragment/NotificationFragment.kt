package com.example.testbottomview.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testbottomview.R
import com.example.testbottomview.test.InlineTest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.notification_fragment.*

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    val viewModel by viewModels<NotificationViewModel>()

    companion object {
        fun newInstance() = NotificationFragment()
        const val TAG: String = "DashboardFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        return inflater.inflate(R.layout.notification_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG, "onActivityCreated: ")
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        Log.i(TAG, "onAttach: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        textView3.text = "$this\ntag:$tag"
        mBtnVideo.setOnClickListener {
            findNavController().navigate(R.id.action_mNotificationFragment_to_videoActivity)
        }
        viewModel.testTailrec()
        lifecycleScope
        val inlineTest = InlineTest().also {
            //it.testInlineFunction()
            //it.testRunningBlock()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }
}