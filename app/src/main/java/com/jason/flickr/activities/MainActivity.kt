package com.jason.flickr.activities

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.jason.flickr.App
import com.jason.flickr.R
import com.jason.flickr.models.SearchViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private var searchText: String? = ""
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).getAppComponent().inject(this)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        intent?.let { handleIntent(intent) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(intent) }
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            viewModel.tags = intent.getStringExtra(SearchManager.QUERY)

            getFlickrFeed()
        }
    }

    private fun getFlickrFeed() {
        viewModel.getFeed().subscribe({ response ->
            response?.items?.let {
                viewModel.items = it
                bindItems()
            } ?: onError(null)
        }, { t: Throwable? -> onError(t) })
    }

    private fun bindItems() {

    }

    private fun onError(t: Throwable?) {
        Log.e(TAG, "Error getting feed.", t)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo((getSystemService(Context.SEARCH_SERVICE) as SearchManager).getSearchableInfo(componentName))
        setupSearchQuery()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.search -> {
                true
            }
            R.id.help -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearchQuery() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Query Inserted", Toast.LENGTH_SHORT).show()
                searchText = query
                resetSearchView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun resetSearchView() {
        hideSoftKeyboard(this)
        searchView.onActionViewCollapsed()
        searchView.clearFocus()
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    companion object {
        val TAG = MainActivity::class.java.name
    }

}
