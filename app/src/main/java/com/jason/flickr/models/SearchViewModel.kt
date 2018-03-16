package com.jason.flickr.models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.jason.flickr.App
import com.jason.flickr.services.FlickrService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    fun getFeed() : Flowable<JsonFlickrFeed> {
        return  service.getPhotos(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}