package com.stathis.moviepedia.models

class ColorModel(
    val name: String,
    val color: String
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}