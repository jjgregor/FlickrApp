package com.jason.flickr.activities

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jason.flickr.App
import com.jason.flickr.R
import com.jason.flickr.adapters.PhotosAdapter
import com.jason.flickr.models.SearchViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: PhotosAdapter
    private var searchText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).getAppComponent().inject(this)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        adapter = PhotosAdapter(viewModel.items)
        swipe_refresh.setOnRefreshListener { getFlickrFeed() }

        if (!viewModel.items.isEmpty()) {
            bindItems()
        }

        intent?.let { handleIntent(intent) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(intent) }
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            viewModel.searchQuery = intent.getStringExtra(SearchManager.QUERY)
            getFlickrFeed()
        }
    }

    private fun getFlickrFeed() {

        if (search_question.visibility == View.VISIBLE) {
            search_question.visibility = View.GONE
        }

        recycler_view.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE

        val feed = if (isNetworkAvailable()) viewModel.getFeedFromApi() else viewModel.getFeedFromDb()

        feed.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.items.isEmpty()) {
                        onError(null)
                    } else {
                        viewModel.items = response.items
                        adapter.updateData(response.items)
                        bindItems()
                    }
                }, { t: Throwable? -> onError(t) })

        if (swipe_refresh.isRefreshing) {
            swipe_refresh.isRefreshing = false
        }

    }

    private fun bindItems() {
        empty_view.visibility = View.GONE
        progress_bar.visibility = View.GONE
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        recycler_view.visibility = View.VISIBLE
        setupItemTouchHelper()
    }

    private fun setupItemTouchHelper() {
        val simpleItemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean
                    = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
                    = adapter.removeItem(viewHolder.layoutPosition)


            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    val p = Paint()
                    val icon: Bitmap
                    p.color = ContextCompat.getColor(this@MainActivity, android.R.color.white)

                    if (dX > 0) {
                        c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX,
                                itemView.bottom.toFloat(), p)
                    } else {
                        c.drawRect(itemView.right.toFloat() + dX, itemView.top.toFloat(),
                                itemView.right.toFloat(), itemView.bottom.toFloat(), p)
                    }

                    if (dX > 0) {
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.tinder_like)
                        val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.tinder_nope)
                        val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchHelper)
        itemTouchHelper.attachToRecyclerView(recycler_view)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected == true
    }

    private fun onError(t: Throwable?) {
        Log.e(TAG, "Error getting feed.", t)
        empty_view.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
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
        val TAG: String = MainActivity::class.java.name
    }

}
