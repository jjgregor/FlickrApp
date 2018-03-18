package com.jason.flickr

import android.app.Application
import android.arch.persistence.room.Room
import com.jason.flickr.dagger.*
import com.jason.flickr.db.FlickrFeedDB

/**
 * Created by Jason on 3/4/18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, FlickrFeedDB::class.java, "flickr-feed-db").build()

        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(getString(R.string.base_url), applicationContext))
                .roomModule(RoomModule(this))
                .build()
    }

    fun getAppComponent(): AppComponent = component

    companion object {
        lateinit var component: AppComponent
        lateinit var database: FlickrFeedDB
    }

}