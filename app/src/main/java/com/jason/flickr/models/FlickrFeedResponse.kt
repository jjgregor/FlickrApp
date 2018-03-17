package com.jason.flickr.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * Created by Jason on 3/7/18.
 */

data class JsonFlickrFeed(var items: ArrayList<FlickrItem>? = ArrayList()) : Serializable

@Entity(tableName = "flickr_item_table")
data class FlickrItem(@PrimaryKey(autoGenerate = true)
                      val id: Long,
                      @ColumnInfo(name = "title")
                      val title: String? = "",
                      @ColumnInfo(name = "link")
                      val link: String? = "",
                      @Embedded
                      val media: MediaObject? = MediaObject(),
                      @ColumnInfo(name = "author")
                      val author: String? = "") : Serializable

data class MediaObject(var m: String? = "") : Serializable

