package com.jason.flickr.db

import android.arch.persistence.room.*
import com.jason.flickr.models.JsonFlickrFeed
import io.reactivex.Flowable

/**
 * Created by Jason on 3/16/18.
 */

@Dao
interface FlickrFeedDao {

    @Query("SELECT * FROM flickr_item_table")
    fun getAllItems(): Flowable<JsonFlickrFeed>

    @Query("SELECT * FROM flickr_item_table WHERE query = :query")
    fun getItemsAt(query: String): Flowable<JsonFlickrFeed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: JsonFlickrFeed)

    @Delete
    fun delete(item: JsonFlickrFeed)

}