package com.stathis.moviepedia.models

class Movies (
    val id:Int,
    val video:Boolean,
    val voteCount:Int,
    val voteAverage:Double,
    val title: String,
    val releaseDate:String,
    val originalLanguage:String,
    val originalTitle:String,
    val genreIds:List<Int>,
    val imgPath:String,
    val adult:Boolean,
    val overview:String,
    val posterPath:String,
    val popularity:Double,
    val mediaType: String
)