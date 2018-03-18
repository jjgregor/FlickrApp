package com.jason.flickr.db

import android.util.Log
import com.jason.flickr.models.JsonFlickrFeed
import com.jason.flickr.services.FlickrService
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Jason on 3/17/18.
 */
class FeedRepository @Inject constructor(val service: FlickrService, val dao: FlickrFeedDao) {

    fun getUsersFromDb(query: String): Flowable<JsonFlickrFeed> =
            dao.getItemsAt(query).doOnNext { Log.d(TAG, "Dispatching ${it.items?.size} users from DB...") }


    fun getUsersFromApi(query: String): Flowable<JsonFlickrFeed> =
            service.getPhotos(query)
                    .doOnNext {
                        Log.d(TAG, "Dispatching ${it.items?.size} users from API...")
                        it.query = query
                        storeUsersInDb(it)
                    }


    private fun storeUsersInDb(feed: JsonFlickrFeed) {
        Flowable.fromCallable { dao.insert(feed) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG, "Inserted ${feed.items?.size} users from API in DB under ${feed.query}...")
                }
    }

    companion object {
        val TAG: String = FeedRepository::class.java.simpleName
    }

}