package com.example.testbottomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testbottomview.fragment.OneFragment
import com.example.testbottomview.fragment.ThreeFragment
import com.example.testbottomview.fragment.TwoFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_view_page2.*

class ViewPage2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_page2)
        mViewPage2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int) =
                when (position) {
                    0 -> OneFragment.newInstance()
                    1 -> TwoFragment.newInstance()
                    else -> ThreeFragment.newInstance()
                }
        }

        TabLayoutMediator(mTabLayout, mViewPage2) { tab, position ->
            when(position){
                0 -> tab.text = "One"
                1 -> tab.text = "Two"
                else -> tab.text = "Three"
            }
        }.attach()
    }
}