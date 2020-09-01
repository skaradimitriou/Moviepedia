package com.stathis.moviepedia

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.models.TvSeries
import com.stathis.moviepedia.recyclerviews.FavoriteMoviesAdapter
import com.stathis.moviepedia.recyclerviews.FavoriteTvSeriesAdapter
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class UserViewModel : ViewModel() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var userPhoto: CircleImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    private var userFavoriteMovies:MutableList<FavoriteMovies> = mutableListOf()
    private var userFavoriteTvSeries:MutableList<FavoriteTvSeries> = mutableListOf()
    private var favoriteMovies : MutableLiveData<MutableList<FavoriteMovies>> = MutableLiveData()
    private var favoriteTvSeries : MutableLiveData<MutableList<FavoriteTvSeries>> = MutableLiveData()

    fun getUserFavoriteMovies(): MutableLiveData<MutableList<FavoriteMovies>> {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        /*viewModel was creating duplicated objects so I clear the list
                            if the list size contains elements inside*/
                        if (userFavoriteMovies.isNotEmpty()){
                            userFavoriteMovies.clear()
                        }
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteMovies::class.java)
                            userFavoriteMovies.add(fav!!)
                            Log.d("i",i.toString())
                            //display data
                        }
                        favoriteMovies.postValue(userFavoriteMovies)
                    }
                }
            })
        return favoriteMovies
    }

    fun getUserFavoriteTvSeries(): MutableLiveData<MutableList<FavoriteTvSeries>> {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        if (userFavoriteTvSeries.isNotEmpty()){
                            userFavoriteTvSeries.clear()
                        }
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteTvSeries::class.java)
                            userFavoriteTvSeries.add(fav!!)
                            Log.d("i",i.toString())
                        }
                        favoriteTvSeries.postValue(userFavoriteTvSeries)
                    }
                }
            })
        return favoriteTvSeries
    }
}