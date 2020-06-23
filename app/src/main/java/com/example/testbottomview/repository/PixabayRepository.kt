package com.example.testbottomview.repository

import com.example.common.ApiResult
import com.example.common.BaseRepository
import com.example.common.bean.Pixabay
import com.example.net.ApiService
import com.example.net.retrofit.service.PixabayService

/**
 * ClassName:      PixabayRepository
 * Description:    PixabayRepository
 * Author:         mwy
 * CreateDate:     2020/6/20 20:52
 */
class PixabayRepository : BaseRepository() {

    private val _key = "17120440-b20cc91293cbce0d16b31fd2e"
    suspend fun LoadPicture(queryKey: String, loadSize: Int, pageNum: Int): ApiResult<Pixabay> {
        return safeApiCall("加载图片失败"){
            val data = ApiService.getApiService<PixabayService>()
                .LoadPicture(_key, queryKey, loadSize, 1)
            executeResponse(data)
        }
    }
}