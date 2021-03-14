package com.stathis.moviepedia.ui.castDetails

import androidx.lifecycle.ViewModel

class CastDetailsViewModel : ViewModel() {

    private val repo = CastDetailsRepository()
    val actorData = repo.actorData

    fun getActorData(actorID : Int){
        repo.getActorsData(actorID)
    }
}