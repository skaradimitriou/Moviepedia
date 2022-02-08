package com.stathis.moviepedia.models.series

import com.stathis.moviepedia.models.LocalModel

data class TvSeriesFeed (
    val page:Int,
    val total_results:Int,
    val total_pages:Int,
    val results:List<TvSeries>
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}