package com.jason.flickr.dagger

import com.jason.flickr.services.FlickrService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Jason on 3/5/18.
 */
@Module
class FlickrServiceModule {

    @Provides
    internal fun provideStackOverflowService(restAdapter: Retrofit) : FlickrService {
        return restAdapter.create(FlickrService::class.java)
    }
}