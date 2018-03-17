package com.jason.flickr.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.jason.flickr.models.FlickrItem
import io.reactivex.Flowable

/**
 * Created by Jason on 3/16/18.
 */

@Dao
interface FlickrFeedDao {

    @Query("SELECT * FROM flickr_item_table")
    fun getAllItems(): Flowable<List<FlickrItem>>

    @Insert
    fun insert(item: FlickrItem)

    @Delete
    fun delete(item: FlickrItem)

}