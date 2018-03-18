package com.jason.flickr.models

import android.arch.lifecycle.ViewModel
import com.jason.flickr.App
import com.jason.flickr.db.FeedRepository
import io.reactivex.Flowable
import java.util.*
import javax.inject.Inject

/**
 * Created by Jason on 3/7/18.
 */
class SearchViewModel : ViewModel() {

    @Inject lateinit var repo: FeedRepository

    init {
        App.component.inject(this)
    }

    var items = ArrayList<FlickrItem>()
    var searchQuery: String = ""

    fun getFeedFromApi(): Flowable<JsonFlickrFeed> = repo.getUsersFromApi(searchQuery)

    fun getFeedFromDb(): Flowable<JsonFlickrFeed> = repo.getUsersFromDb(searchQuery)

}