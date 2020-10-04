package com.stathis.moviepedia.adapters

import android.view.View
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries

interface ItemClickListener : View.OnClickListener {
    fun onItemClick(movies: Movies)
    fun onTvSeriesClick(tvSeries: TvSeries)
}