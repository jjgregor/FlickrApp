package com.jason.flickr.dagger

import com.jason.flickr.activities.MainActivity
import com.jason.flickr.models.SearchViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Jason on 3/4/18.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, FlickrServiceModule::class))
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(searchViewModel: SearchViewModel)
}