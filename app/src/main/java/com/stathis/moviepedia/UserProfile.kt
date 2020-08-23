package com.stathis.moviepedia

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.recyclerviews.FavoriteClickListener
import com.stathis.moviepedia.recyclerviews.FavoriteMoviesAdapter
import com.stathis.moviepedia.recyclerviews.FavoriteTvSeriesAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.ByteArrayOutputStream

class UserProfile : AppCompatActivity(), FavoriteClickListener {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var userPhoto: CircleImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    private var userFavoriteMovies:MutableList<FavoriteMovies> = mutableListOf()
    private var userFavoriteTvSeries:MutableList<FavoriteTvSeries> = mutableListOf()
    private lateinit var favGridRecView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val favMovies:TextView = findViewById(R.id.favMov)
        val favTv:TextView = findViewById(R.id.favTv)

        getUserFavoriteMovies()

        favMovies.setOnClickListener {
            favMovies.alpha = 1F
            favTv.alpha = 0.5F
            getUserFavoriteMovies()
        }

        favTv.setOnClickListener{
            favTv.alpha = 1F
            favMovies.alpha = 0.5F
            getUserFavoriteTvSeries()
        }

        userPhoto = findViewById(R.id.profileImg)
        retrieveUserImg()

        favGridRecView = findViewById(R.id.favGridRecView)

        userPhoto.setOnClickListener{
            takePictureIntent()
        }
    }


    private fun getUserFavoriteMovies(){
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovies")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteMovies::class.java)
                            userFavoriteMovies.add(fav!!)
                            Log.d("i",i.toString())

                            //display data
                            if (userFavoriteMovies.size > 0){
                                favGridRecView.adapter = FavoriteMoviesAdapter(userFavoriteMovies,this@UserProfile)
                            }
                        }
                    }
                }
            })
    }

    private fun getUserFavoriteTvSeries(){
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
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteTvSeries::class.java)
                            userFavoriteTvSeries.add(fav!!)
                            Log.d("i",i.toString())
                            if (userFavoriteTvSeries.size > 0){
                                favGridRecView.adapter = FavoriteTvSeriesAdapter(userFavoriteTvSeries,this@UserProfile)
                            }
                        }
                    }
                }
            })
    }

    private fun retrieveUserImg(){
        storage = FirebaseStorage.getInstance()
        var imageRef: StorageReference = storage.reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        imageRef.getBytes(1024*1024).addOnSuccessListener {bytes ->
            var userPhoto:CircleImageView = findViewById(R.id.profileImg)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
            userPhoto.setImageBitmap(bitmap)

        }.addOnFailureListener {
            // Handle any errors
        }
        imageRef.downloadUrl.addOnSuccessListener {downloadUrl ->
            Log.d("DownloadUrl", downloadUrl.toString())
        }.addOnFailureListener {it->
            Log.d("DownloadUrl", it.toString())
        }

    }

    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ pictureIntent ->
            pictureIntent.resolveActivity(this.packageManager!!)?.also{
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadAndSaveBitmapUri(imgBitmap)
        }
    }

    private fun uploadAndSaveBitmapUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)
        upload.addOnCompleteListener{uploadTask ->
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                    urlTask.result?.let{
                        imageUri = it
                        userPhoto.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let{
                    //
                }
            }
        }
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        val movieIntent = Intent (this, MovieInfoScreen::class.java)
        //converting rating toString() so I can pass it. Double was throwing an error
        val rating = favoriteMovies.movie_rating.toString()
        movieIntent.putExtra("MOVIE_NAME", favoriteMovies.title)
        movieIntent.putExtra("MOVIE_PHOTO",favoriteMovies.photo)
        movieIntent.putExtra("RELEASE_DATE",favoriteMovies.releaseDate)
        movieIntent.putExtra("DESCRIPTION",favoriteMovies.description)
        movieIntent.putExtra("RATING",rating)
        startActivity(movieIntent)
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        val movieIntent = Intent (this, TvSeriesInfoScreen::class.java)
        //converting rating toString() so I can pass it. Double was throwing error
        val rating = favoriteTvSeries.movie_rating.toString()
        Log.d("rating", rating)
        movieIntent.putExtra("TV_SERIES_NAME",favoriteTvSeries.title )
        movieIntent.putExtra("TV_SERIES_PHOTO",favoriteTvSeries.photo)
        movieIntent.putExtra("TV_SERIES_ID",favoriteTvSeries.id)
        movieIntent.putExtra("TV_SERIES_RELEASE_DATE",favoriteTvSeries.releaseDate)
        movieIntent.putExtra("TV_SERIES_DESCRIPTION",favoriteTvSeries.description)
        movieIntent.putExtra("TV_SERIES_RATING",rating)
        startActivity(movieIntent)
    }

}