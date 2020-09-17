package com.stathis.moviepedia.userProfile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityUserProfileBinding
import com.stathis.moviepedia.loginAndRegister.IntroScreen
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.movieInfoScreen.MovieInfoScreen
import com.stathis.moviepedia.recyclerviews.FavoriteAdapter
import com.stathis.moviepedia.recyclerviews.FavoriteClickListener
import com.stathis.moviepedia.tvSeriesInfoScreen.TvSeriesInfoScreen
import java.io.ByteArrayOutputStream
import kotlin.math.log

class UserProfile : AppCompatActivity(), FavoriteClickListener {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var auth: FirebaseAuth
    private var userViewModel: UserViewModel =
        UserViewModel()
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // User ViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        userViewModel.getUserFavoriteMovies()

        userViewModel.getUserFavoriteMovies()
            .observe(this, Observer<MutableList<FavoriteMovies>> { t ->
                Log.d("User Fav Movies", t.toString())
                if (t.size > 0) {
                    favoriteAdapter = FavoriteAdapter(this@UserProfile)
                    favoriteAdapter.submitList(t as List<Any>?)
                    binding.favGridRecView.adapter = favoriteAdapter
                } else {
                    //display empty favorites
                }
            })

        userViewModel.getUserFavoriteTvSeries()
            .observe(this, Observer<MutableList<FavoriteTvSeries>> { c ->
                Log.d("User Fav Tv Series", c.toString())
                if (c.size > 0) {
                    favoriteAdapter = FavoriteAdapter(this@UserProfile)
                    favoriteAdapter.submitList(c as List<Any>?)
                    binding.favGridRecView.adapter = favoriteAdapter
                } else {
                    //display empty favorites
                }
            })

        userViewModel.retrieveUserImg().observe(this, Observer<Bitmap> { img ->
            Log.d("profile image path", img.toString())
            binding.profileImg.setImageBitmap(img)
        })

        binding.favMov.setOnClickListener {
            binding.favMov.alpha = 1F
            binding.favTv.alpha = 0.5F
            userViewModel.getUserFavoriteMovies()
        }

        binding.logout.setOnClickListener {
            askForLogout()
        }

        binding.favTv.setOnClickListener {
            binding.favTv.alpha = 1F
            binding.favMov.alpha = 0.5F
            userViewModel.getUserFavoriteTvSeries()
        }

        userViewModel.retrieveUserImg()

        binding.profileImg.setOnClickListener {
            takePictureIntent()
        }
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(this.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadAndSaveBitmapUri(imgBitmap)
        }
    }

    private fun uploadAndSaveBitmapUri(bitmap: Bitmap) {
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
                        binding.profileImg.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    //
                }
            }
        }
    }

    private fun askForLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Do you want to log out?")

        builder.setPositiveButton("YES") { dialog, which ->
            logoutUser()
        }

        builder.setNegativeButton("CANCEL") { dialog, which ->
        }
        builder.show()
    }

    private fun logoutUser() {
        //add a progress bar for logout experience
        val loadingDialog = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        val builder = AlertDialog.Builder(this).setView(loadingDialog)
        builder.setCancelable(true)
        builder.show()
        //3 sec delay
        Handler().postDelayed({
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(this, IntroScreen::class.java))
            overridePendingTransition(0, 0)
            finish()
        },3000)
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        val movieIntent = Intent(this, MovieInfoScreen::class.java)
        //converting rating toString() so I can pass it. Double was throwing an error
        val rating = favoriteMovies.movie_rating.toString()
        movieIntent.putExtra("MOVIE_NAME", favoriteMovies.title)
        movieIntent.putExtra("MOVIE_PHOTO", favoriteMovies.photo)
        movieIntent.putExtra("RELEASE_DATE", favoriteMovies.releaseDate)
        movieIntent.putExtra("DESCRIPTION", favoriteMovies.description)
        movieIntent.putExtra("RATING", rating)
        startActivity(movieIntent)
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        val movieIntent = Intent(this, TvSeriesInfoScreen::class.java)
        //converting rating toString() so I can pass it. Double was throwing error
        val rating = favoriteTvSeries.movie_rating.toString()
        Log.d("rating", rating)
        movieIntent.putExtra("TV_SERIES_NAME", favoriteTvSeries.title)
        movieIntent.putExtra("TV_SERIES_PHOTO", favoriteTvSeries.photo)
        movieIntent.putExtra("TV_SERIES_ID", favoriteTvSeries.id)
        movieIntent.putExtra("TV_SERIES_RELEASE_DATE", favoriteTvSeries.releaseDate)
        movieIntent.putExtra("TV_SERIES_DESCRIPTION", favoriteTvSeries.description)
        movieIntent.putExtra("TV_SERIES_RATING", rating)
        startActivity(movieIntent)
    }

}