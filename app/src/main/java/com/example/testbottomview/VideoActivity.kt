package com.example.testbottomview

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.base.L
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.video_controller_layout.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            //获取到视频尺寸后,视频长度也可以拿到了  赋值给seekBar的max
            seekBar.max = viewModel.mediaPlayer.duration
            L.i("video duration:${viewModel.mediaPlayer.duration}")
            playFrame.post {
                resizePlayer(it.first,it.second)
            }
        })
        viewModel.controllerPanelVisibility.observe(this@VideoActivity, Observer {
            mFlControllerPanel.visibility = it
        })

        viewModel.bufferPercent.observe(this@VideoActivity, Observer {
            seekBar.secondaryProgress = seekBar.max * it / 100
            L.i("secondaryProgress : ${seekBar.secondaryProgress}")
        })

        viewModel.progress.observe(this@VideoActivity, Observer {
            seekBar.progress = it
        })

        viewModel.playerStatus.observe(this@VideoActivity, Observer {
            mIvPoP.isClickable = true
            when(it){
                PlayerStatus.Pause -> mIvPoP.setImageResource(R.drawable.ic_video_play)
                PlayerStatus.Completed -> {
                    mIvPoP.setImageResource(R.drawable.ic_video_play)
                    mFlControllerPanel.visibility = View.VISIBLE
                }
                PlayerStatus.NotReady -> mIvPoP.isClickable = false
                else -> mIvPoP.setImageResource(R.drawable.ic_video_pause)
            }
        })

        viewModel.replayVisibility.observe(this@VideoActivity, Observer {
            mIvReplay.visibility = it
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
                    viewModel.emmitVideoResolution()
                    setDisplay(holder)
                    //播放时屏幕常亮
                    setScreenOnWhilePlaying(true)
                }
            }
        })
        playFrame.setOnClickListener {
            viewModel.toggleControllerVisibility()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    viewModel.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        mIvReplay.setOnClickListener {
            viewModel.togglePlayerStatus()
            mIvReplay.visibility = View.INVISIBLE
        }
        mIvPoP.setOnClickListener {
            viewModel.togglePlayerStatus()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
            viewModel.emmitVideoResolution()
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
        //SYSTEM_UI_FLAG_IMMERSIVE_STICKY 全屏后 点击屏幕出现系统状态栏 随后又自动隐藏
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
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