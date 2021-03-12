package com.stathis.moviepedia.ui.genresInfoScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.UpcomingMovies
import com.stathis.moviepedia.network.RetrofitApiClient
import retrofit2.Callback

class GenresRepository {

    val movies = MutableLiveData<List<Movies>>()

    fun getResultsForThisGenre(genreId: Int) {
        RetrofitApiClient.getResultsForThisGenre(genreId).enqueue(object : Callback<UpcomingMovies> {
            override fun onResponse(
                call: retrofit2.Call<UpcomingMovies>,
                response: retrofit2.Response<UpcomingMovies>
            ) {
                Log.d("", response.body().toString())
                movies.postValue(response.body()?.results)
            }

            override fun onFailure(call: retrofit2.Call<UpcomingMovies>, t: Throwable) {
                movies.postValue(null)
            }
        })
    }
}