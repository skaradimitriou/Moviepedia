package com.stathis.moviepedia.ui.castDetails

import androidx.lifecycle.MutableLiveData
import com.stathis.moviepedia.models.actor.Actor
import com.stathis.moviepedia.models.actor.KnownMoviesFeed
import com.stathis.moviepedia.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CastDetailsRepository {

    val actorData = MutableLiveData<Actor>()
    val knownMovies = MutableLiveData<KnownMoviesFeed>()

    fun getActorsData(actorID: Int) {
        ApiClient.getActorInfo(actorID).enqueue(object : Callback<Actor> {
            override fun onResponse(call: Call<Actor>, response: Response<Actor>) {
                actorData.value = response.body()
            }

            override fun onFailure(call: Call<Actor>, t: Throwable) {
                actorData.value = null
            }
        })
    }

    fun getActorsKnownMovies(actorID: Int) {
        ApiClient.getActorsKnownMovies(actorID).enqueue(object : Callback<KnownMoviesFeed> {
            override fun onResponse(call: Call<KnownMoviesFeed>, response: Response<KnownMoviesFeed>) {
                knownMovies.value = response.body()
            }

            override fun onFailure(call: Call<KnownMoviesFeed>, t: Throwable) {
                knownMovies.value = null
            }
        })
    }
}