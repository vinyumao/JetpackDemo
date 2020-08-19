package com.example.testbottomview

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * ClassName:      MyMediaPlayer
 * Description:    MyMediaPlayer
 * Author:         mwy
 * CreateDate:     2020/8/19 18:12
 */
class MyMediaPlayer : MediaPlayer(),LifecycleObserver{
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pausePlayer(){
        pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumePlayer(){
        start()
    }

}