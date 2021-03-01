package com.stathis.moviepedia.ui.movieInfoScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MoviesInfoRepository {

    private lateinit var url: String
    private lateinit var request: Request
    private var client = OkHttpClient()
    val castInfo = MutableLiveData<List<Cast>>()
    val reviews = MutableLiveData<List<Reviews>>()
    val apiKey = "api_key=b36812048cc4b54d559f16a2ff196bc5"

    fun getMovieCastInfo(movieId: Int) {
        url =
            "https://api.themoviedb.org/3/movie/$movieId/credits?$apiKey"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val cast = GsonBuilder().create().fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", cast.toString())

                if (cast?.cast != null) {
                    castInfo.postValue(cast.cast)
                }
            }
        })
    }

    fun getMovieReviews(movieId: Int) {
        url =
            "https://api.themoviedb.org/3/movie/$movieId/reviews?$apiKey"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val review = GsonBuilder().create().fromJson(body, ReviewsFeed::class.java)
                Log.d("Response", review.toString())

                if (review.results != null) {
                    reviews.postValue(review.results)
                }
            }
        })
    }
}