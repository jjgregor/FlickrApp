package com.jason.flickr.dagger


import android.app.Application
import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jason.flickr.BuildConfig
import com.jason.flickr.util.ObjectMapperFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

/**
 * Created by Jason on 3/3/18.
 */
@Module
class NetworkModule(private val baseUrl: String, internal val context: Context) {

    @Provides
    @Singleton
    internal fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val ongoing = chain.request().newBuilder()
                    ongoing.addHeader("api_key", "3a4e049045a2705d2070931771b996d9")
                    chain.proceed(ongoing.build())
                }
                .addNetworkInterceptor(if (BuildConfig.DEBUG) StethoInterceptor() else null)
                .cache(cache)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapperFactory().getObjectMapper()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }
}