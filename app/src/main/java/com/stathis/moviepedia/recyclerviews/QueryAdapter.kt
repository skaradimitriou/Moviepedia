package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.search.Query
import com.stathis.moviepedia.models.search.SearchItem

class QueryAdapter(private val searchItems: MutableList<Query>,private val listener: SearchItemClickListener): RecyclerView.Adapter<QueryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.query_item_row, parent, false)
        return QueryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        val currentItem = searchItems[position]
        holder.bind(currentItem,listener)
    }
}