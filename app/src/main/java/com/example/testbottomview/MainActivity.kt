package com.example.testbottomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_main_fragment)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment

        navController.navigatorProvider.addNavigator(FixFragmentNavigator(this,supportFragmentManager,fragment.id))
        navController.setGraph(R.navigation.nav_main)
        val configuration = AppBarConfiguration.Builder(mBottomBar.menu).build()
        setupActionBarWithNavController(navController,configuration)
        mBottomBar.setupWithNavController(navController)
    }
}