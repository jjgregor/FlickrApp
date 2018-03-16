package com.jason.flickr.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jason.flickr.R
import com.jason.flickr.models.FlickrItem
import kotlinx.android.synthetic.main.photo_card_view.view.*
import java.util.regex.Pattern


/**
 * Created by Jason on 3/13/18.
 */
class PhotosAdapter(private val items: ArrayList<FlickrItem>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_card_view, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(data: ArrayList<FlickrItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(item: FlickrItem) {
            with(item){
                Glide.with(itemView.context)
                        .load(media?.m)
                        .apply(RequestOptions()
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(itemView.photo)
                itemView.title.text = title
                itemView.author.text = parseAuthor(author)
            }
        }

        private fun parseAuthor(author: String?): String {
            author?.let {
                val pattern = Pattern.compile("\"([^\"]*)\"")
                val m = pattern.matcher(author)

                if (m.find()) {
                    return m.group(1)
                }
            }
            return ""

        }
    }
}

