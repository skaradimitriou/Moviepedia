package com.stathis.moviepedia.ui.dashboard.search.models

import android.os.Parcelable
import com.stathis.moviepedia.models.LocalModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class SearchItem(
    val id: Int,
    val video: Boolean,
    val vote_count: Int,
    val name: String,
    val vote_average: Double,
    val title: String,
    val release_date: String,
    val original_language: String,
    val original_title: String,
    val genre_ids: List<Int>,
    val backdrop_path: String,
    val adult: Boolean,
    val overview: String,
    val poster_path: String,
    val popularity: Double,
    val media_type: String,
    val original_name: String,
    val origin_country: List<String>,
    val first_air_date: String
) : Parcelable, LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}