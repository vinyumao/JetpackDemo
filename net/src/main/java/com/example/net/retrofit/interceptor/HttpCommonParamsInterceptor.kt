package com.example.net.retrofit.interceptor

import com.example.base.utlis.ApplicationUtils
import com.example.common.BaseApp
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException

/**
 * 描述: 公共参数拦截器
 * --------------------------------------------
 * 工程:
 * #0000     @author mwy     创建日期: 2018-05-07 18:01
 */
class HttpCommonParamsInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()
        val formBody: RequestBody = FormBody.Builder()
            //.add("version", "" + ApplicationUtils.getVerCode(BaseApp.instance))
            .add("version", "" + ApplicationUtils.getVerCode(BaseApp.instance))
            .build()
        var postBodyString =
            bodyToString(request.body)
        postBodyString += (if (postBodyString.isNotEmpty()) "&" else "") + bodyToString(
            formBody
        )
        request = requestBuilder
            .post(
                postBodyString
                    .toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull())
            )
            .build()
        return chain.proceed(request)
    }

    companion object {
        private fun bodyToString(request: RequestBody?): String {
            return try {
                val buffer = Buffer()
                if (request != null) {
                    request.writeTo(buffer)
                } else {
                    return ""
                }
                buffer.readUtf8()
            } catch (e: IOException) {
                "did not work"
            }
        }
    }
}