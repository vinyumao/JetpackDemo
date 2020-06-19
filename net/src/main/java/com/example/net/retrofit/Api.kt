package com.example.net.retrofit

import android.content.Context
import com.example.base.L
import com.example.base.utlis.ApplicationUtils
import com.example.base.utlis.CacheUtils
import com.example.common.BaseApp
import com.example.net.retrofit.interceptor.HttpCommonParamsInterceptor
import com.orhanobut.logger.Logger
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * ClassName:      Api
 * Description:    网络请求工具
 * Author:         mwy
 * CreateDate:     2020/6/19 16:31
 */
class Api private constructor(){

    companion object{
        const val HOST = "https://pixabay.com/"
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            Api()
        }
    }

    lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient
    private var loggingInterceptor:Interceptor

    init {
        /**
         * 打印JSON的拦截器
         */
       loggingInterceptor = invoke {
            val request = it.request()
            val requestBuffer = Buffer()
            if (request.body != null) {
                request.body!!.writeTo(requestBuffer)
            } else {
                L.d("request.body() == null")
            }
            //打印url信息
            val response = it.proceed(request)
            //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，原因在于这个方法会消耗返回结果的数据(buffer)
            val responseBody = response.body
            //为了不消耗buffer，我们这里使用source先获得buffer对象，然后clone()后使用
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            //获得返回的数据
            val buffer = source.buffer
            //使用前clone()下，避免直接消耗
            Logger.json(buffer.clone().readString(Charset.forName("UTF-8")))

            response
        }
        initOkHttpClient(BaseApp.instance)
        initRetrofit()
    }

    /**
     * 初始化Retrofit
     */
    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 初始化OkHttp
     */
    private fun initOkHttpClient(context: Context) {
        val cache = Cache(File(CacheUtils.getCacheDir(context), "HttpCache"), 1024 * 1024 * 80)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .retryOnConnectionFailure(true)
            //添加全局请求参数
            // .addInterceptor(HttpCommonParamsInterceptor())
                //全局添加请求头
            .addInterceptor(invoke {
                val build = it.request().newBuilder()
                    .addHeader("version", ApplicationUtils.getVerName(BaseApp.instance))
                    .build()
                it.proceed(build)
            })
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}