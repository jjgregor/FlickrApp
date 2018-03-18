package com.jason.flickr.dagger

import com.jason.flickr.activities.MainActivity
import com.jason.flickr.db.FeedRepository
import com.jason.flickr.db.FlickrFeedDB
import com.jason.flickr.db.FlickrFeedDao
import com.jason.flickr.models.SearchViewModel
import com.jason.flickr.services.FlickrService
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Jason on 3/4/18.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, FlickrServiceModule::class, RoomModule::class))
interface AppComponent {

    fun productDao(): FlickrFeedDao

    fun flickrFeedDB(): FlickrFeedDB

    fun flickerService(): FlickrService

    fun feedRepository(): FeedRepository

    fun inject(mainActivity: MainActivity)

    fun inject(searchViewModel: SearchViewModel)

}