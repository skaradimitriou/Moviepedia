package com.stathis.moviepedia.ui.genresInfoScreen

import com.stathis.moviepedia.network.ApiClient

class GenresRepository {

    val movies = ApiClient.movies

    fun getResultsForThisGenre(genreId: Int) {
        ApiClient.getResultsForThisGenre(genreId)
    }
}