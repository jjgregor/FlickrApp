package com.jason.flickr.services

import com.jason.flickr.models.JsonFlickrFeed
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Jason on 3/5/18.
 */
interface FlickrService {

    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    fun getPhotos(@Query("tags") tag: String?): Flowable<JsonFlickrFeed>

}