package com.jason.flickr.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.jason.flickr.models.JsonFlickrFeed

/**
 * Created by Jason on 3/17/18.
 */
@Database(entities = arrayOf(JsonFlickrFeed::class), version = 1)
abstract class FlickrFeedDB : RoomDatabase() {
    abstract fun flickrItemDao(): FlickrFeedDao
}