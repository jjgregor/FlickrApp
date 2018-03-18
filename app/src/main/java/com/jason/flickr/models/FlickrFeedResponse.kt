package com.jason.flickr.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Created by Jason on 3/7/18.
 */

@Entity(tableName = "flickr_item_table")
data class JsonFlickrFeed(@Embedded
                          var items: ArrayList<FlickrItem> = ArrayList(),
                          @PrimaryKey
                          var query: String = "") : Serializable

data class FlickrItem(val title: String = "",
                      val link: String = "",
                      @Embedded
                      val media: MediaObject = MediaObject(),
                      val author: String = "") : Serializable

data class MediaObject(var m: String = "") : Serializable

