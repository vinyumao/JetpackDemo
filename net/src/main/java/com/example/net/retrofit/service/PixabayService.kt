package com.example.net.retrofit.service

import com.example.bean.pixabay.Pixabay
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * ClassName:      PixabayService
 * Description:    PixabayService
 * Author:         mwy
 * CreateDate:     2020/6/19 17:36
 */
interface PixabayService {
    @GET("api/")
    suspend fun LoadPicture(@Query("key") key :String,
                            @Query("q") queryKey: String,
                            @Query("per_page") pageSize: Int,
                            @Query("page") pageNum: Int): Pixabay
}