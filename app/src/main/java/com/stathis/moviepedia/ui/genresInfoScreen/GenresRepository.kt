package com.stathis.moviepedia.ui.genresInfoScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.GenreMoviesFeed
import com.stathis.moviepedia.models.Movies
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GenresRepository {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private val apiKey = "?api_key=b36812048cc4b54d559f16a2ff196bc5"
    val movies: MutableLiveData<MutableList<Movies>> = MutableLiveData()

    fun getResultsForThisGenre(genreId: Int) {
        url = "https://api.themoviedb.org/3/discover/movie$apiKey&with_genres=$genreId"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val genres = GsonBuilder().create().fromJson(body, GenreMoviesFeed::class.java)
                Log.d("Response", genres.toString())
                movies.postValue(ArrayList(genres.results))
            }
        })
    }
}