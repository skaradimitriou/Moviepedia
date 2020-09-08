package com.stathis.moviepedia.tvSeriesInfoScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import com.stathis.moviepedia.recyclerviews.CastAdapter
import com.stathis.moviepedia.recyclerviews.ReviewsAdapter
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class TvSeriesInfoScreenViewModel : ViewModel() {

    private lateinit var url: String
    private lateinit var request: Request
    private var client: OkHttpClient = OkHttpClient()
    private var castInfo: MutableList<Cast> = mutableListOf()
    private var cast: MutableLiveData<MutableList<Cast>> = MutableLiveData()
    private var reviews: MutableLiveData<MutableList<Reviews>> = MutableLiveData()

    fun getCastInfo(tvSeriesId: Int): MutableLiveData<MutableList<Cast>> {
        url =
            "https://api.themoviedb.org/3/tv/$tvSeriesId/credits?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val castFeed = gson.fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", castFeed.toString())

                if (cast != null) {
                    castInfo = ArrayList(castFeed.cast)
                    Log.d("Cast", castInfo.toString())
                    cast.postValue(castInfo)
//                    runOnUiThread {
//                        binding.castRecView.adapter = CastAdapter(castInfo)
//                    }
                }
            }
        })
        return cast
    }

    fun getTvSeriesReviews(tvSeriesId: Int): MutableLiveData<MutableList<Reviews>> {
        url =
            "https://api.themoviedb.org/3/tv/$tvSeriesId/reviews?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val review = gson.fromJson(body, ReviewsFeed::class.java)
                Log.d("Response", review.toString())

                if (review.results != null) {
                    val reviewsFeed = ArrayList(review.results)
                    Log.d("this is the list",reviewsFeed.toString())
                    //display the reviews
//                    val reviewsAdapter = ReviewsAdapter()
//                    reviewsAdapter.submitList(reviewsFeed as List<Any>?)
                    reviews.postValue(reviewsFeed)
//                    runOnUiThread{
//                        binding.reviewsRecView.adapter = reviewsAdapter
//                    }
                }
            }
        })
        return reviews
    }

}