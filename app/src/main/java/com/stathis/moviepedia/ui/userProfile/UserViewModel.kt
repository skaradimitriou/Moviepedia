package com.stathis.moviepedia.ui.userProfile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stathis.moviepedia.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File

class UserViewModel : ViewModel() {

    private lateinit var imageUri: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    private var userFavoriteMovies: MutableList<FavoriteMovies> = mutableListOf()
    private var userFavoriteTvSeries: MutableList<FavoriteTvSeries> = mutableListOf()
    private var favoriteMovies: MutableLiveData<MutableList<FavoriteMovies>> = MutableLiveData()
    private var favoriteTvSeries: MutableLiveData<MutableList<FavoriteTvSeries>> = MutableLiveData()
    private var userImgPath: MutableLiveData<Bitmap> = MutableLiveData()
    private var username: MutableLiveData<String> = MutableLiveData()
    private var downloadUrl: MutableLiveData<Bitmap> = MutableLiveData()
    private lateinit var emptyModelList: MutableList<EmptyModel>
    private lateinit var imgUrl: String
    private var imageDownloadLink: MutableLiveData<String> = MutableLiveData()

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
                    } else {
                        Log.d("Empty Favorites", "Empty Favorites")
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
                    } else {
                        Log.d("Empty Favorites", "Empty Favorites")
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
        return username
    }

    fun uploadAndSaveBitmapUri(bitmap: Bitmap): MutableLiveData<Bitmap> {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)
        upload.addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        downloadUrl.postValue(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    //
                }
            }
        }
        return downloadUrl
    }

    fun uploadAndSavePhoto(uri: Uri) {

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

    fun savePhotoToDb(string: String) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userPhoto").setValue(string)
            .addOnSuccessListener { it ->

            }
        getUserPhoto()
    }

    fun getUserPhoto(): MutableLiveData<String> {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("userPhoto")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        imgUrl = p0.value.toString()
                        Log.d("URL", imgUrl)
                        imageDownloadLink.postValue(imgUrl)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    //
                }

            })
        return imageDownloadLink
    }

}