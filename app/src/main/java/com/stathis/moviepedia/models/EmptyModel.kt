package com.stathis.moviepedia.models

class EmptyModel (
    val text:String
) : LocalModel{
    override fun equals(model: LocalModel): Boolean = false
}