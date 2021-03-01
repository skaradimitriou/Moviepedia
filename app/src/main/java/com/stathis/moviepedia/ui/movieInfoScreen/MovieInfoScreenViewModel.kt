package com.stathis.moviepedia.ui.movieInfoScreen

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.stathis.moviepedia.adapters.CastAdapter
import com.stathis.moviepedia.adapters.ReviewsAdapter
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.Reviews
import com.stathis.moviepedia.models.ReviewsFeed
import com.stathis.moviepedia.models.cast.Cast
import com.stathis.moviepedia.models.cast.MovieCastFeed
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MovieInfoScreenViewModel : ViewModel() {

    private val repo = MoviesInfoRepository()
    private var castInfo = repo.castInfo
    private var reviews = repo.reviews

    val adapter = CastAdapter()
    val reviewsAdapter = ReviewsAdapter()

    init {
        adapter.submitList(
            mutableListOf(
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel(""),
                EmptyModel("")
            ) as List<Any>?
        )
    }

    fun getMovieCastInfo(movieId : Int){
        repo.getMovieCastInfo(movieId)
    }

    fun getMovieReviews(movieId: Int) {
        repo.getMovieReviews(movieId)
    }

//    fun getMovieCastInfo(movieId: Int) {
//        url =
//            "https://api.themoviedb.org/3/movie/$movieId/credits?api_key=b36812048cc4b54d559f16a2ff196bc5"
//        request = Request.Builder().url(url).build()
//
//        client = OkHttpClient()
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d("Call Failed", call.toString())
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                val body = response.body?.string()
//                val gson = GsonBuilder().create()
//                val cast = gson.fromJson(body, MovieCastFeed::class.java)
//                Log.d("Response", cast.toString())
//
//                if (cast?.cast != null) {
//                    Log.d("this is the list", movieCastInfo.toString())
//                    castInfo.postValue(cast.cast)
//                }
//            }
//        })
//    }
//
//    fun getMovieReviews(movieId: Int) {
//        url =
//            "https://api.themoviedb.org/3/movie/$movieId/reviews?api_key=b36812048cc4b54d559f16a2ff196bc5"
//        request = Request.Builder().url(url).build()
//
//        client = OkHttpClient()
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d("Call Failed", call.toString())
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                val body = response.body?.string()
//                val gson = GsonBuilder().create()
//                val review = gson.fromJson(body, ReviewsFeed::class.java)
//                Log.d("Response", review.toString())
//
//                if (review.results != null) {
//                    val movieReviewsFeed = ArrayList(review.results)
//                    Log.d("this is the list", movieReviewsFeed.toString())
//                    //display the reviews
//                    reviews.postValue(movieReviewsFeed)
//                }
//            }
//        })
//    }

    fun observeData(owner: LifecycleOwner) {
        castInfo.observe(owner, Observer { cast ->
            Log.d("CAST", cast.toString())
            adapter.submitList(cast as List<Any>?)
            adapter.notifyDataSetChanged()
        })

        reviews.observe(owner, Observer {
            reviewsAdapter.submitList(it as List<Any>?)
        })
    }
}