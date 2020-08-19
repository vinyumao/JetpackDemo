package com.example.testbottomview

import android.app.Application
import android.media.MediaPlayer
import android.view.View
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.base.L
import com.example.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * ClassName:      VideoViewModel
 * Description:    VideoViewModel
 * Author:         mwy
 * CreateDate:     2020/8/19 15:51
 */
@ActivityRetainedScoped
class VideoViewModel @ViewModelInject constructor(
     application: Application,
     val mediaPlayer: MyMediaPlayer,
     @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    private var _progressBarVisibility = MutableLiveData(View.VISIBLE)
    var progressBarVisibility: LiveData<Int> = _progressBarVisibility

    //视频分辨率
    private var _videoResolution = MutableLiveData(Pair(0, 0))
    var videoResolution: LiveData<Pair<Int, Int>> = _videoResolution

    init {
        L.i("mediaPlayer:$mediaPlayer")
        loadVideo()
    }

    private fun loadVideo() {
        mediaPlayer.apply {
            reset()
            _progressBarVisibility.value = View.VISIBLE
            setDataSource("http://upos-sz-mirrorcos.bilivideo.com/upgcxcode/51/11/214301151/214301151-1-208.mp4?e=ig8euxZM2rNcNbN17WdVhwdlhbRBhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1597844646&gen=playurl&os=cosbv&oi=837317101&trid=394c4f4621dd4fdf93d9cbf1fd6e54cfT&platform=html5&upsig=4fc32aaca46599dfa8cae2c810ea2cd9&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&orderid=0,1&logo=80000000")
            setOnPreparedListener {
                _progressBarVisibility.value = View.INVISIBLE
                isLooping = true
                it.start()
            }
            setOnErrorListener { mp, what, extra ->
                L.i("mediaPlayerError(what:$what,extra:$extra)")
                false
            }
            //视频尺寸变化时,保存视频分辨率
            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolution.value = Pair(width, height)
            }
            prepareAsync()
        }
    }

    override fun onCleared() {
        super.onCleared()
        L.i("VideoModel onCleared")
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}