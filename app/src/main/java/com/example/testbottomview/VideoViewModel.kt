package com.example.testbottomview

import android.app.Application
import android.media.MediaPlayer
import android.view.View
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.base.L
import com.example.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.notify
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

    //loadingView 显示隐藏
    private var _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility: LiveData<Int> = _progressBarVisibility

    //视频分辨率
    private var _videoResolution = MutableLiveData(Pair(0, 0))
    val videoResolution: LiveData<Pair<Int, Int>> = _videoResolution

    //控制面板可见
    private var _controllerPanelVisibility = MutableLiveData(View.INVISIBLE)
    val controllerPanelVisibility: LiveData<Int> = _controllerPanelVisibility

    //视频缓冲进度 百分比
    private var _bufferPercent = MutableLiveData(0)
    val bufferPercent =_bufferPercent

    //视频播放进度
    private var _progress = MutableLiveData(mediaPlayer.currentPosition)
    val progress =_progress

    //视频播放状态
    private var _playerStatus = MutableLiveData(PlayerStatus.NotReady)
    val playerStatus = _playerStatus

    //重播按钮可见状态
    private var _replayVisibility = MutableLiveData(View.INVISIBLE)
    val replayVisibility = _replayVisibility

    private var controllerShowTime : Long = 0

    init {
        L.i("mediaPlayer:$mediaPlayer")
        loadVideo()
    }

    private fun loadVideo() {
        mediaPlayer.apply {
            reset()
            _playerStatus.value = PlayerStatus.NotReady
            _progressBarVisibility.value = View.VISIBLE
            //b站视频在线解析地址 https://xbeibeix.com/bilibili2/
            //备用在线地址 https://stream7.iqilu.com/10339/upload_transcode/202008/13/20200813062558C3XtyuHXsn.mp4
            setDataSource("https://stream7.iqilu.com/10339/upload_transcode/202008/13/20200813062558C3XtyuHXsn.mp4")
            setOnPreparedListener {
                _progressBarVisibility.value = View.INVISIBLE
                _replayVisibility.value = View.INVISIBLE
                //是否自动循环播放
                //isLooping = true
                it.start()
                _playerStatus.value = PlayerStatus.Playing
                updatePlayerProgress()
            }
            setOnErrorListener { _, what, extra ->
                L.i("mediaPlayerError(what:$what,extra:$extra)")
                false
            }
            //视频尺寸变化时,保存视频分辨率
            setOnVideoSizeChangedListener { _, width, height ->
                _videoResolution.value = Pair(width, height)
            }

            setOnBufferingUpdateListener { _, percent ->
                _bufferPercent.value = percent
                L.i("buffer percent : $percent")
            }

            setOnSeekCompleteListener {
                _progressBarVisibility.value = View.INVISIBLE
            }

            setOnCompletionListener {
                _playerStatus.value = PlayerStatus.Completed
                _controllerPanelVisibility.value = View.VISIBLE
                //视频时长大于0 视频正确加载完成 才显示
                if (mediaPlayer.duration > 0){
                    _replayVisibility.value = View.VISIBLE
                }
            }
            prepareAsync()
        }
    }

    //切换播放状态
    fun togglePlayerStatus(){
        when(_playerStatus.value){
            PlayerStatus.Playing -> {
                mediaPlayer.pause()
                _playerStatus.value = PlayerStatus.Pause
            }
            PlayerStatus.Pause,PlayerStatus.Completed -> {
                mediaPlayer.start()
                _playerStatus.value = PlayerStatus.Playing
                _controllerPanelVisibility.value = View.INVISIBLE
            }
             else -> return
        }
    }

    //定时刷新进度条
    private fun updatePlayerProgress(){
        viewModelScope.launch {
            while (true){
                delay(500)
                _progress.value = mediaPlayer.currentPosition
            }
        }
    }

    //发送视频分辨率信息
    fun emmitVideoResolution() {
        //通过赋值给自己,更改值来发送消息
        _videoResolution.value = _videoResolution.value
    }

    //显示隐藏播放控制面板
    fun toggleControllerVisibility() {
        if (_controllerPanelVisibility.value == View.INVISIBLE) {
            _controllerPanelVisibility.value = View.VISIBLE
            controllerShowTime = System.currentTimeMillis()
            //3秒后自动消失
            viewModelScope.launch {
                delay(3000)
                //防止反复点击 只在最后一次点击 并且是可见状态下的时候 倒计时隐藏
                if (System.currentTimeMillis() - controllerShowTime >= 3000){
                    _controllerPanelVisibility.value = View.INVISIBLE
                }
            }
        } else {
            _controllerPanelVisibility.value = View.INVISIBLE
        }
    }

    //拖动进度条
    fun seekTo(progress: Int){
        _progressBarVisibility.value = View.VISIBLE
        mediaPlayer.seekTo(progress)
    }

    override fun onCleared() {
        super.onCleared()
        L.i("VideoModel onCleared")
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}