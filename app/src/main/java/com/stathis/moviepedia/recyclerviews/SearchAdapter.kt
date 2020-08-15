package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.models.search.SearchItem

class SearchAdapter(private val searchItems: ArrayList<SearchItem>) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item_row, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = searchItems[position]
        holder.bind(currentItem)
    }
}