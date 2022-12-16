package com.colin.request

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream

/**
Date:2022-02-24
Time:15:54
author:colin
 * 用来拦截登陆时候的验证码用的
 */
@GlideModule
class HttpGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        // 请求拦截器

        // 请求拦截器
        val requestInterceptor = RequestInterceptor()
        val mClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()

        // 注意这里用我们刚才现有的Client实例传入即可

        // 注意这里用我们刚才现有的Client实例传入即可
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java, OkHttpUrlLoader.Factory(mClient)
        )

    }

}