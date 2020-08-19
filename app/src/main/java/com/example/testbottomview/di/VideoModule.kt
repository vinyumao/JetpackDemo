package com.example.testbottomview.di

import android.media.MediaPlayer
import com.example.testbottomview.MyMediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * ClassName:      VideoModule
 * Description:    VideoModule
 * Author:         mwy
 * CreateDate:     2020/8/19 16:00
 */
@Module
@InstallIn(ApplicationComponent::class)
object VideoModule {
    @Provides
    fun provideMyMediaPlayer(): MyMediaPlayer = MyMediaPlayer()
}