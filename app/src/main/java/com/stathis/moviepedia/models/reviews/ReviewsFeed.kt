package com.stathis.moviepedia.models.reviews

import com.stathis.moviepedia.models.LocalModel

class ReviewsFeed(
    val id: Int,
    val page: Int,
    val results: List<Reviews>,
    val total_pages: Int,
    val total_results: Int
) : LocalModel {
    override fun equals(model: LocalModel): Boolean = false
}