package com.stathis.moviepedia.ui.movieInfoScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MovieInfoScreenViewModel : ViewModel() {

    private lateinit var url: String
    private lateinit var request: Request
    private lateinit var client: OkHttpClient
    private var movieCastInfo: MutableList<Cast> = mutableListOf()
    private var castInfo : MutableLiveData<MutableList<Cast>> = MutableLiveData()
    private var reviews : MutableLiveData<MutableList<Reviews>> = MutableLiveData()

    fun getMovieCastInfo(movieId: Int): MutableLiveData<MutableList<Cast>> {
        url =
            "https://api.themoviedb.org/3/movie/$movieId/credits?api_key=b36812048cc4b54d559f16a2ff196bc5"
        request = Request.Builder().url(url).build()

        client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Call Failed", call.toString())
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val cast = gson.fromJson(body, MovieCastFeed::class.java)
                Log.d("Response", cast.toString())

                if (cast?.cast != null) {
                    movieCastInfo = ArrayList(cast.cast)
                    Log.d("this is the list", movieCastInfo.toString())
                    castInfo.postValue(movieCastInfo)
//                    runOnUiThread{
//                        binding.castRecView.adapter = CastAdapter(movieCastInfo)
//                    }
                }
            }
        })
        return castInfo
    }

    fun getMovieReviews(movieId: Int): MutableLiveData<MutableList<Reviews>> {
        url =
            "https://api.themoviedb.org/3/movie/$movieId/reviews?api_key=b36812048cc4b54d559f16a2ff196bc5"
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
                    val movieReviewsFeed = ArrayList(review.results)
                    Log.d("this is the list",movieReviewsFeed.toString())
                    //display the reviews
                    reviews.postValue(movieReviewsFeed)
//                    runOnUiThread{
//                        binding.reviewsRecView.adapter = reviewsAdapter
//                    }
                }
            }
        })
        return reviews
    }
}