package com.stathis.moviepedia.ui.userProfile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.ui.userProfile.holder.FavoriteAdapter
import com.stathis.moviepedia.listeners.old.FavoriteClickListener
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries

class UserViewModel : ViewModel(), FavoriteClickListener {

    private val repo = UserProfileRepository()
    val adapter = FavoriteAdapter(this)
    private val favoriteMovies = repo.favoriteMovies
    private val favoriteTvSeries = repo.favoriteTvSeries
    val imageDownloadLink = repo.imageDownloadLink
    val username = repo.username
    private lateinit var emptyModelList: MutableList<EmptyModel>

    init {
        startShimmer()
    }

    fun startShimmer(): MutableList<EmptyModel> {
        emptyModelList = mutableListOf(
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel(""),
            EmptyModel("")
        )
        return emptyModelList
    }

    fun observeData(owner: LifecycleOwner) {
        favoriteMovies.observe(owner, Observer {
            adapter.submitList(it as List<Any>?)
            adapter.notifyDataSetChanged()
        })

        favoriteTvSeries.observe(owner, Observer {
            adapter.submitList(it as List<Any>?)
            adapter.notifyDataSetChanged()
        })
    }

    fun removeObservers(owner: LifecycleOwner) {
        favoriteMovies.removeObservers(owner)
        favoriteTvSeries.removeObservers(owner)
        imageDownloadLink.removeObservers(owner)
        username.removeObservers(owner)
    }

    fun getUserFavoriteMovies() {
        repo.getUserFavoriteMovies()
    }

    fun getUserFavoriteTvSeries() {
        repo.getUserFavoriteTvSeries()
    }

    fun savePhotoToDb(string: String) {
        repo.savePhotoToDb(string)
    }

    fun getUserPhoto() {
        repo.getUserPhoto()
    }

    fun retrieveUsername() {
        repo.retrieveUsername()
    }

    fun saveCameraPhotoToDb(bitmap: Bitmap) {
        repo.saveCameraPhotoToDb(bitmap)
    }

    fun saveGalleryPhotoToDb(uri: Uri) {
        repo.saveGalleryPhotoToDb(uri)
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        //
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        //
    }
}