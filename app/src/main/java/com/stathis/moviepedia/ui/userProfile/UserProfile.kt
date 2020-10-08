package com.stathis.moviepedia.ui.userProfile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityUserProfileBinding
import com.stathis.moviepedia.ui.loginAndRegister.IntroScreen
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.ui.movieInfoScreen.MovieInfoScreen
import com.stathis.moviepedia.adapters.FavoriteAdapter
import com.stathis.moviepedia.adapters.FavoriteClickListener
import com.stathis.moviepedia.ui.tvSeriesInfoScreen.TvSeriesInfoScreen
import kotlinx.android.synthetic.main.bottom_sheet_choose_option.view.*

class UserProfile : AppCompatActivity(), FavoriteClickListener {

    private val REQUEST_IMAGE_CAPTURE = 100
    private val IMAGE_PICK_CODE = 200;
    private val PERMISSION_CODE = 201;
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

        binding.backButton.setOnClickListener{ onBackPressed() }

        favoriteAdapter = FavoriteAdapter(this@UserProfile)

        //handling onClickListener for included layout
        binding.logout.apply {
            binding.logout.logoutIcon.setOnClickListener { askForLogout() }
            binding.logout.logoutText.setOnClickListener { askForLogout() }
        }

        userViewModel.retrieveUsername().observe(this,Observer<String> { username ->
            Log.d("username", username.toString())
            if(username.toString().isNullOrEmpty()){
                binding.profileName.text = "User"
            }else {
                binding.profileName.text = username.toString()
            }
        })

        userViewModel.retrieveUserImg().observe(this, Observer<Bitmap> { img ->
            Log.d("profile image path", img.toString())
            binding.profileImg.setImageBitmap(img)
        })

        startShimmer()
        observeUserFavoriteTvSeries()

        binding.favMov.setOnClickListener {
            binding.favMov.alpha = 1F
            binding.favTv.alpha = 0.5F

            startShimmer()
            observerUserFavMovies()
        }

        binding.favTv.setOnClickListener {
            binding.favTv.alpha = 1F
            binding.favMov.alpha = 0.5F

            startShimmer()
            observeUserFavoriteTvSeries()

        }

        binding.profileImg.setOnClickListener {
            // show Bottom Sheet Fragment
            showUploadPhotoOptions()
        }
    }

    private fun startShimmer(){
        favoriteAdapter.submitList(userViewModel.startShimmer() as List<Any>?)
        favoriteAdapter.notifyDataSetChanged()
    }


    private fun showUploadPhotoOptions() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_choose_option, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()
        view.uploadFromCamera.setOnClickListener {
            takePictureIntent()
        }
        view.uploadFromGallery.setOnClickListener {
            uploadFromGallery()
        }
    }

    private fun observerUserFavMovies(){
        userViewModel.getUserFavoriteMovies()
            .observe(this, Observer<MutableList<FavoriteMovies>> { t ->
                Log.d("User Fav Movies", t.toString())
                if (t.size > 0) {
                    favoriteAdapter.submitList(t as List<Any>?)
                    favoriteAdapter.notifyDataSetChanged()
                    binding.favGridRecView.adapter = favoriteAdapter
                } else {
                    //display empty favorites
                }
            })
    }

    private fun observeUserFavoriteTvSeries(){
        userViewModel.getUserFavoriteTvSeries()
            .observe(this, Observer<MutableList<FavoriteTvSeries>> { c ->
                Log.d("User Fav Tv Series", c.toString())
                if (c.size > 0) {
                    favoriteAdapter.submitList(c as List<Any>?)
                    binding.favGridRecView.adapter = favoriteAdapter
                } else {
                    //display empty favorites
                }
            })
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(this.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun uploadFromGallery() {
        Intent(Intent.ACTION_PICK).also { uploadFromCamera ->
            uploadFromCamera.resolveActivity(this.packageManager!!)?.also {
                startActivityForResult(uploadFromCamera, IMAGE_PICK_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            userViewModel.uploadAndSaveBitmapUri(imgBitmap)
        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            userViewModel.uploadAndSaveBitmapUri(imgBitmap)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    uploadFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
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
            dialog.dismiss()
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