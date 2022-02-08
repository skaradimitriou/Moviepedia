package com.stathis.moviepedia.models.reviews

import com.stathis.moviepedia.models.LocalModel

data class Reviews(
    val id:String,
    val author:String,
    val content:String,
    val url:String
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}