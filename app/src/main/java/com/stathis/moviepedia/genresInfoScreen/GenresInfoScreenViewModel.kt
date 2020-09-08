package com.stathis.moviepedia.genresInfoScreen

import android.graphics.Color
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.databinding.ActivityGenresInfoScreenBinding
import com.stathis.moviepedia.models.GenreMoviesFeed
import com.stathis.moviepedia.models.MovieGenres
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.recyclerviews.MoviesAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GenresInfoScreenViewModel : ViewModel() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private val apiKey = "?api_key=b36812048cc4b54d559f16a2ff196bc5"
    private var movieList: MutableList<Movies> = mutableListOf()
    private var movies: MutableLiveData<MutableList<Movies>> = MutableLiveData()
    private lateinit var binding: ActivityGenresInfoScreenBinding

    fun getResultsForThisGenre(genreId: Int): MutableLiveData<MutableList<Movies>> {
        url = "https://api.themoviedb.org/3/discover/movie$apiKey&with_genres=$genreId"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val genres = gson.fromJson(body, GenreMoviesFeed::class.java)
                Log.d("Response", genres.toString())
                movieList = ArrayList(genres.results)
                Log.d("GENRE_LIST", movieList.toString())
                movies.postValue(movieList)
            }
        })
        return movies
    }

}