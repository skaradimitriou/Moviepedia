package com.stathis.moviepedia.userProfile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stathis.moviepedia.loginAndRegister.IntroScreen
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries

class UserViewModel : ViewModel() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var userFavoriteMovies: MutableList<FavoriteMovies> = mutableListOf()
    private var userFavoriteTvSeries: MutableList<FavoriteTvSeries> = mutableListOf()
    private var favoriteMovies: MutableLiveData<MutableList<FavoriteMovies>> = MutableLiveData()
    private var favoriteTvSeries: MutableLiveData<MutableList<FavoriteTvSeries>> = MutableLiveData()
    private var userImgPath: MutableLiveData<Bitmap> = MutableLiveData()
    private var username: MutableLiveData<String> = MutableLiveData()

    fun getUserFavoriteMovies(): MutableLiveData<MutableList<FavoriteMovies>> {
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        /*viewModel was creating duplicated objects so I clear the list
                            if the list size contains elements inside*/
                        if (userFavoriteMovies.isNotEmpty()) {
                            userFavoriteMovies.clear()
                        }
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteMovies::class.java)
                            userFavoriteMovies.add(fav!!)
                            Log.d("i", i.toString())
                            //display data
                        }
                        favoriteMovies.postValue(userFavoriteMovies)
                    }
                }
            })
        return favoriteMovies
    }

    fun getUserFavoriteTvSeries(): MutableLiveData<MutableList<FavoriteTvSeries>> {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        if (userFavoriteTvSeries.isNotEmpty()) {
                            userFavoriteTvSeries.clear()
                        }
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteTvSeries::class.java)
                            userFavoriteTvSeries.add(fav!!)
                            Log.d("i", i.toString())
                        }
                        favoriteTvSeries.postValue(userFavoriteTvSeries)
                    }
                }
            })
        return favoriteTvSeries
    }

    fun retrieveUserImg(): MutableLiveData<Bitmap> {
        storage = FirebaseStorage.getInstance()
        var imageRef: StorageReference =
            storage.reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        imageRef.getBytes(1024 * 1024).addOnSuccessListener { bytes ->

            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            userImgPath.postValue(bitmap)
//            userPhoto.setImageBitmap(bitmap)

        }.addOnFailureListener {
            // Handle any errors
        }
        imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
            Log.d("DownloadUrl", downloadUrl.toString())

        }.addOnFailureListener { it ->
            Log.d("DownloadUrl", it.toString())
        }
        return userImgPath
    }

    fun retrieveUsername(): MutableLiveData<String> {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("username")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        for (i in p0.children){
                            username.postValue(i.value.toString())
                        }
                    }
                }
            })

        return username
    }
}