package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.R
import com.stathis.moviepedia.listeners.ItemClickListener
import com.stathis.moviepedia.models.TvSeries

class AiringTvSeriesAdapter(private val listener: ItemClickListener) :
    ListAdapter<Any, AiringTvSeriesViewHolder>(DiffUtilClass<Any>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiringTvSeriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        return AiringTvSeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AiringTvSeriesViewHolder, position: Int) {
        holder.bind(getItem(position) as TvSeries, listener)
    }
}