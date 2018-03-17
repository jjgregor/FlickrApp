package com.jason.flickr

import android.app.Application
import android.arch.persistence.room.Room
import com.jason.flickr.dagger.AppComponent
import com.jason.flickr.dagger.AppModule
import com.jason.flickr.dagger.DaggerAppComponent
import com.jason.flickr.dagger.NetworkModule
import com.jason.flickr.db.FlickrFeedDB

/**
 * Created by Jason on 3/4/18.
 */
class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, FlickrFeedDB::class.java, "flickr-feed-db").build()

        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(getString(R.string.base_url), applicationContext))
                .build()
    }

    fun getAppComponent(): AppComponent = component

    companion object {
        var database: FlickrFeedDB? = null
    }

}