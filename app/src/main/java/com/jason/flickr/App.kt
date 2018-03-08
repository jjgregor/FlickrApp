package com.jason.flickr

import android.app.Application
import com.jason.flickr.dagger.AppComponent
import com.jason.flickr.dagger.AppModule
import com.jason.flickr.dagger.DaggerAppComponent
import com.jason.flickr.dagger.NetworkModule

/**
 * Created by Jason on 3/4/18.
 */
class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(getString(R.string.base_url), applicationContext))
                .build()
    }

    fun getAppComponent(): AppComponent = component

}