package com.jason.flickr.dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Jason on 3/4/18.
 */
@Module
class AppModule(var application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

}