package com.stathis.moviepedia.models

class FavoriteMovies(
    val id:Int,
    val photo:String,
    val title:String,
    val movie_rating:Double,
    val description:String,
    val releaseDate:String
){
    constructor():this (0,"","",0.0,"","")
}