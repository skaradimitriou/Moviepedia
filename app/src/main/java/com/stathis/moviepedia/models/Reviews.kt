package com.stathis.moviepedia.models

data class Reviews(
    val id:String,
    val author:String,
    val content:String,
    val url:String
) : LocalModel