package com.example.testbottomview

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.base.L
import com.example.testbottomview.fragment.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_main_fragment)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment

        navController.navigatorProvider.addNavigator(
            FixFragmentNavigator(
                this,
                supportFragmentManager,
                fragment.id
            )
        )
        navController.setGraph(R.navigation.nav_main)
        val configuration = AppBarConfiguration.Builder(mBottomBar.menu).build()
        setupActionBarWithNavController(navController, configuration)
        mBottomBar.setupWithNavController(navController)
        //去掉长按菜单 弹出菜单文字Toast
        mBottomBar.getChildAt(0).findViewById<View>(R.id.mHomeFragment)
            .setOnLongClickListener { true }
        mBottomBar.getChildAt(0).findViewById<View>(R.id.mDashboardFragment)
            .setOnLongClickListener { true }
        mBottomBar.getChildAt(0).findViewById<View>(R.id.mNotificationFragment)
            .setOnLongClickListener { true }
    }
}