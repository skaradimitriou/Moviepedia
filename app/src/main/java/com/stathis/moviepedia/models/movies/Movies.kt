package com.stathis.moviepedia.models.movies

import android.os.Parcelable
import com.stathis.moviepedia.models.LocalModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class Movies (
    val id:Int,
    val video:Boolean,
    val vote_count:Int,
    val name:String,
    val vote_average:Double,
    val title: String,
    val release_date:String,
    val original_language:String,
    val original_title:String,
    val genre_ids: List<Int>,
    val backdrop_path:String,
    val adult:Boolean,
    val overview:String,
    val poster_path:String,
    val popularity:Double,
    val media_type: String
) : Parcelable, LocalModel {
    constructor() : this(0,false,0,"",0.0,""
        ,"","","", emptyList(),"",false
        ,"","",0.0,"")
    override fun equals(model: LocalModel): Boolean = false
}