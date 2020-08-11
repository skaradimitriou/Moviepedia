package com.stathis.moviepedia.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.TvSeriesFeed

class AiringTvSeriesAdapter(val airingTvSeries: TvSeriesFeed, private val listener: ItemClickListener) : RecyclerView.Adapter<AiringTvSeriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiringTvSeriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_item_row, parent, false)
        return AiringTvSeriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return airingTvSeries.results.size
    }

    override fun onBindViewHolder(holder: AiringTvSeriesViewHolder, position: Int) {
        val currentItem = airingTvSeries.results[position]
        holder.bind(currentItem,listener)
    }
}