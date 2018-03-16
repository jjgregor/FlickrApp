package com.jason.flickr.models

import java.io.Serializable
import java.util.*

/**
 * Created by Jason on 3/7/18.
 */

data class JsonFlickrFeed(var items: ArrayList<FlickrItem>? = ArrayList()) : Serializable

data class FlickrItem(val title: String? = "",
                      val link: String? = "",
                      val media: MediaObject? = MediaObject(),
                      val author: String? = "") : Serializable

data class MediaObject(val m: String? = "") : Serializable

