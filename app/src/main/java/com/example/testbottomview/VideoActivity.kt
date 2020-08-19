package com.example.testbottomview

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_video.*

/**
 * ClassName:      VideoActivity
 * Description:    VideoActivity
 * Author:         mwy
 * CreateDate:     2020/8/19 15:43
 */
@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {
    val viewModel by viewModels<VideoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        lifecycle.addObserver(viewModel.mediaPlayer)
        viewModel.progressBarVisibility.observe(this@VideoActivity, Observer {
            progressBar.visibility = it
        })
        viewModel.videoResolution.observe(this@VideoActivity, Observer {
            playFrame.post {
                resizePlayer(it.first,it.second)
            }
        })
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {}
            override fun surfaceDestroyed(holder: SurfaceHolder?) {}
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                viewModel.mediaPlayer.apply {
                    //如果切换视频重新设置surfaceView尺寸
                    resizePlayer(viewModel.videoResolution.value!!.first,viewModel.videoResolution.value!!.second)
                    setDisplay(holder)
                    //播放时屏幕常亮
                    setScreenOnWhilePlaying(true)
                }
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
        }else{
            showSystemUI()
        }
    }

    //重新设置surfaceView尺寸
    private fun resizePlayer(with: Int, height: Int) {
        if (with <= 0 || height <= 0) return
        surfaceView.layoutParams = FrameLayout.LayoutParams(
            playFrame.height * with / height, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
    }

    //全屏
    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    //退出全屏
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}