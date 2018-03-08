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

    var items = ArrayList<FlickrItems>()
    var tags: String = ""

    @Inject lateinit var service: FlickrService

    init {
        (application as App).getAppComponent().inject(this)
    }

    fun getFeed() : Observable<JsonFlickrFeed> {
        return  service.getPhotos(getFormattedTags(tags))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getFormattedTags(tags: String): String? {
        return tags.replace(" ", ",")
    }

}