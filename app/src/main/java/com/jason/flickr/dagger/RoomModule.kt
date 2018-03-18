package com.jason.flickr.dagger

import android.app.Application
import android.arch.persistence.room.Room
import com.jason.flickr.db.FeedRepository
import com.jason.flickr.db.FlickrFeedDB
import com.jason.flickr.db.FlickrFeedDao
import com.jason.flickr.services.FlickrService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Jason on 3/17/18.
 */

@Module
class RoomModule(application: Application) {

    private val flickrFeedDb: FlickrFeedDB = Room.databaseBuilder(application, FlickrFeedDB::class.java, "flickr-feed-db").build()

    @Singleton
    @Provides
    internal fun providesRoomDatabase(): FlickrFeedDB {
        return flickrFeedDb
    }

    @Singleton
    @Provides
    internal fun providesProductDao(db: FlickrFeedDB): FlickrFeedDao {
        return db.flickrItemDao()
    }

    @Singleton
    @Provides
    internal fun productRepository(service: FlickrService, dao: FlickrFeedDao): FeedRepository {
        return FeedRepository(service, dao)
    }

}