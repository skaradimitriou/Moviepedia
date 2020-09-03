package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

class TopRatedAdapter(val listener:ItemClickListener) :
    ListAdapter<Any, RecyclerView.ViewHolder>(DiffUtilClass<Any>()) {

    val VIEW_TYPE_ONE = 1
    val VIEW_TYPE_TWO = 2

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE) {
            TopRatedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.top_rated_item_row, parent, false))
        } else AiringTvSeriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row,parent,false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem is Movies) { // put your condition, according to your requirements
            (holder as TopRatedViewHolder).bind(currentItem,listener)
        } else {
            (holder as AiringTvSeriesViewHolder).bind(currentItem as TvSeries,listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return if(currentItem is Movies){
            VIEW_TYPE_ONE
        } else {
            VIEW_TYPE_TWO
        }
    }
}