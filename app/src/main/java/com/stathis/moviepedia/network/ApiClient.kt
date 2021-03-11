package com.stathis.moviepedia.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.API_KEY
import com.stathis.moviepedia.BASE_URL
import com.stathis.moviepedia.models.GenreMoviesFeed
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.Query
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItem
import com.stathis.moviepedia.ui.dashboard.fragments.search.models.SearchItemsFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object ApiClient {

    private lateinit var url: String
    private lateinit var request: Request
    private var client: OkHttpClient = OkHttpClient()
    val cast = MutableLiveData<MutableList<Cast>>()
    val reviews = MutableLiveData<MutableList<Reviews>>()
    val movies = MutableLiveData<List<Movies>>()
    val recentSearches = MutableLiveData<MutableList<SearchItem>>()

    fun getMovieCastInfo(movieId: Int) {
        url = "$BASE_URL/movie/$movieId/credits?$API_KEY"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val castFeed = GsonBuilder().create().fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", castFeed.toString())

                if (castFeed?.cast != null) {
                    cast.postValue(ArrayList(castFeed.cast))
                }
            }
        })
    }

    fun getMovieReviews(movieId: Int) {
        url = "$BASE_URL/movie/$movieId/reviews?$API_KEY"
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
                    reviews.postValue(ArrayList(review.results))
                }
            }
        })
    }

    fun getCastInfo(tvSeriesId: Int) {
        url = "$BASE_URL/tv/$tvSeriesId/credits?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val castFeed = GsonBuilder().create().fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", castFeed.toString())

                if (castFeed.cast != null) {
                    cast.postValue(ArrayList(castFeed.cast))
                }
            }
        })
    }

    fun getTvSeriesReviews(tvSeriesId: Int) {
        url = "$BASE_URL/tv/$tvSeriesId/reviews?$API_KEY"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val review = GsonBuilder().create().fromJson(body, ReviewsFeed::class.java)

                if (review.results != null) {
                    reviews.postValue(ArrayList(review.results))
                }
            }
        })
    }

    fun getResultsForThisGenre(genreId: Int) {
        url = "$BASE_URL/discover/movie?$API_KEY&with_genres=$genreId"
        request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val genres = GsonBuilder().create().fromJson(body, GenreMoviesFeed::class.java)
                movies.postValue(genres.results)
            }
        })
    }

//    fun getQueryInfo(query: Query) {
//        url = "$BASE_URL/search/multi?$API_KEY&query=${query.queryName}"
//        request = Request.Builder().url(url).build()
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                //
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body?.string()
//                val searchItem = GsonBuilder().create().fromJson(body, SearchItemsFeed::class.java)
//                recentSearches.postValue(ArrayList(searchItem.results))
//            }
//        })
//    }
}