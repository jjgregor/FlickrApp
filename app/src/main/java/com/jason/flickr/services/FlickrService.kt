package com.jason.flickr.services

import com.jason.flickr.models.JsonFlickrFeed
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable


/**
 * Created by Jason on 3/5/18.
 */
interface FlickrService {

    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    fun getPhotos(@Query("text") tags: String?): Observable<JsonFlickrFeed>

}