package com.stathis.moviepedia.models.series

import android.os.Parcelable
import com.stathis.moviepedia.models.LocalModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvSeries (

    val original_name:String,
    val genre_ids:List<Int>,
    val name:String,
    val popularity:Double,
    val origin_country:List<String>,
    val vote_count:Int,
    val first_air_date:String,
    val backdrop_path:String?,
    val original_language:String,
    val id: Int,
    val vote_average:Double,
    val overview:String,
    val poster_path:String
) : Parcelable, LocalModel {
    override fun equals(model: LocalModel): Boolean = false

    constructor() : this("",emptyList(),"",0.0, emptyList(),0,""
        ,"","",0, 0.0,"","")
}