package com.stathis.moviepedia.ui.dashboard.fragments.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.DiffUtilClassV2
import com.stathis.moviepedia.databinding.HolderEmptyLayoutBinding
import com.stathis.moviepedia.databinding.HolderMovieItemBigBinding
import com.stathis.moviepedia.databinding.HolderTvSeriesItemBigBinding
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries

class FavoritesAdapter(val callback : GenericCallback) : ListAdapter<LocalModel, FavoritesViewHolder>(DiffUtilClassV2<LocalModel>()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when(viewType){
            R.layout.holder_movie_item_big -> HolderMovieItemBigBinding.inflate(layoutInflater,parent,false)
            R.layout.holder_tv_series_item_big -> HolderTvSeriesItemBigBinding.inflate(layoutInflater,parent,false)
            else -> HolderEmptyLayoutBinding.inflate(layoutInflater,parent,false)
        }

        return FavoritesViewHolder(view,callback)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.present(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when(getItem(position)){
        is Movies -> R.layout.holder_movie_item_big
        is TvSeries -> R.layout.holder_tv_series_item_big
        else -> R.layout.holder_empty_layout
    }
}