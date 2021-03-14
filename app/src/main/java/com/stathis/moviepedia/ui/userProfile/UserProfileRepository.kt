package com.stathis.moviepedia.ui.userProfile

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import java.io.ByteArrayOutputStream

class UserProfileRepository {

    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }
    val favoriteMovies = MutableLiveData<MutableList<FavoriteMovies>>()
    val favoriteTvSeries = MutableLiveData<MutableList<FavoriteTvSeries>>()
    val imageDownloadLink = MutableLiveData<String>()
    val username = MutableLiveData<String>()

    fun getUserFavoriteMovies() {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val userFavoriteMovies = mutableListOf<FavoriteMovies>()
                        p0.children.forEach {
                            val fav = it.getValue(FavoriteMovies::class.java)
                            userFavoriteMovies.add(fav!!)
                            Log.d("i", it.toString())
                        }

                        favoriteMovies.postValue(userFavoriteMovies)
                    } else {
                        Log.d("Empty Favorites", "Empty Favorites")
                        favoriteMovies.postValue(null)
                    }
                }
            })
    }

    fun getUserFavoriteTvSeries() {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteTvSeries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {

                        val userFavoriteTvSeries = mutableListOf<FavoriteTvSeries>()
                        p0.children.forEach {
                            val fav = it.getValue(FavoriteTvSeries::class.java)
                            userFavoriteTvSeries.add(fav!!)
                            Log.d("i", it.toString())
                        }

                        favoriteTvSeries.postValue(userFavoriteTvSeries)
                    } else {
                        Log.d("Empty Favorites", "Empty Favorites")
                        favoriteMovies.postValue(null)
                    }
                }
            })
    }

    fun savePhotoToDb(string: String) {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userPhoto").setValue(string)
        getUserPhoto()
    }

    fun getUserPhoto() {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userPhoto")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val imgUrl = p0.value.toString()
                        Log.d("URL", imgUrl)
                        imageDownloadLink.postValue(imgUrl)
                    } else {
                        imageDownloadLink.postValue(null)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    //
                }

            })
    }

    fun retrieveUsername() {
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("username")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("p0", "p0")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        Log.d("P0", "")
                        val name = p0.value
                        Log.d("NAME", name.toString())
                        username.postValue(name.toString())
                    }
                }
            })
    }

    fun saveCameraPhotoToDb(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)
        upload.addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    val imageLink = it.toString()
                    Log.d("MSG", imageLink)
                    savePhotoToDb(imageLink)
                }
            }
    }

    fun saveGalleryPhotoToDb(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        val upload = storageRef.putFile(uri)
        upload.addOnSuccessListener {
            val result = it.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                val imageLink = it.toString()
                Log.d("MSG", imageLink)
                savePhotoToDb(imageLink)
            }
        }
    }
}