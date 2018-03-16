package com.jason.flickr.models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.jason.flickr.App
import com.jason.flickr.services.FlickrService
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Jason on 3/7/18.
 */
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    var items = ArrayList<FlickrItem>()
    var searchQuery: String = ""

    @Inject lateinit var service: FlickrService

    init {
        (application as App).getAppComponent().inject(this)
    }

    fun getFeed() : Observable<JsonFlickrFeed> {
        return  service.getPhotos(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}