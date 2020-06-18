package com.example.testbottomview.fragment

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testbottomview.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OneViewModel(application: Application) : AndroidViewModel(application) {
    val app: MyApp by lazy { getApplication<MyApp>() }

    fun saveToPhoto(bitmap: Bitmap){
        viewModelScope.launch {
            //获取contentResolver 插入存储卡图片的 uri
            val insertUri = app.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )?: kotlin.run {
                return@launch
            }
            withContext(Dispatchers.IO){
                //用contentResolver 来把图片保存到系统相册
                //.use 可以自动关闭Stream
                app.contentResolver.openOutputStream(insertUri).use {
                    if(bitmap.compress(Bitmap.CompressFormat.JPEG,90,it)){
                        MainScope().launch {
                            Toast.makeText(app, "保存图片成功", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        MainScope().launch {
                            Toast.makeText(app, "保存图片失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}