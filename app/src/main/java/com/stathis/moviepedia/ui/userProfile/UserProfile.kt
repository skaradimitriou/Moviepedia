package com.stathis.moviepedia.ui.userProfile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.AbstractActivity
import com.stathis.moviepedia.databinding.ActivityUserProfileBinding
import kotlinx.android.synthetic.main.bottom_sheet_choose_option.view.*

class UserProfile : AbstractActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private val IMAGE_PICK_CODE = 200
    private val PERMISSION_CODE = 201
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initLayout() {
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun running() {
        binding.favGridRecView.adapter = viewModel.adapter

        startShimmer()

        viewModel.getUserFavoriteMovies()
        viewModel.retrieveUsername()
        viewModel.getUserPhoto()

        binding.backButton.setOnClickListener { onBackPressed() }

        binding.logout.apply {
            binding.logout.logoutIcon.setOnClickListener { askForLogout() }
            binding.logout.logoutText.setOnClickListener { askForLogout() }
        }

        binding.favMov.setOnClickListener {
            binding.favMov.alpha = 1F
            binding.favTv.alpha = 0.5F

            viewModel.getUserFavoriteMovies()
            startShimmer()
        }

        binding.favTv.setOnClickListener {
            binding.favTv.alpha = 1F
            binding.favMov.alpha = 0.5F

            viewModel.getUserFavoriteTvSeries()
            startShimmer()
        }

        binding.profileImg.setOnClickListener {
            showUploadPhotoOptions()
        }

        observeViewModel()
    }

    override fun stopped() {
        viewModel.removeObservers(this)
    }

    private fun startShimmer() {
        viewModel.adapter.submitList(viewModel.startShimmer() as List<Any>?)
    }

    private fun observeViewModel() {
        viewModel.observeData(this)

        viewModel.username.observe(this, Observer { username ->
            Log.d("username", username.toString())
            if (username.toString().isNullOrEmpty()) {
                binding.profileName.text = "User"
            } else {
                binding.profileName.text = username.toString()
            }
        })

        viewModel.imageDownloadLink.observe(this, Observer<String> { img ->
            Log.d("profile image path", img.toString())
            if (img != null) {
                Glide.with(this).load(img).into(binding.profileImg)
            }
        })
    }

    private fun showUploadPhotoOptions() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_choose_option, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()
        view.uploadFromCamera.setOnClickListener {
            takePictureIntent()
            dialog.dismiss()
        }
        view.uploadFromGallery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    uploadFromGallery()
                }
            } else {
                uploadFromGallery()
            }
            dialog.dismiss()
        }
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(this.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun uploadFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            viewModel.saveCameraPhotoToDb(imgBitmap)

        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // I have to save the url to the db
            val imageUri = data?.data
            if (imageUri != null) {
                viewModel.saveGalleryPhotoToDb(imageUri)
                viewModel.getUserPhoto()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    uploadFromGallery()
                } else {
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
            //startActivity(Intent(this, IntroScreen::class.java))
            overridePendingTransition(0, 0)
            finish()
        }, 3000)
    }

//    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
//        startActivity(Intent(this, MovieInfoScreen::class.java).apply {
//            putExtra("MOVIE_ID", favoriteMovies.id)
//            putExtra("MOVIE_NAME", favoriteMovies.title)
//            putExtra("MOVIE_PHOTO", favoriteMovies.photo)
//            putExtra("RELEASE_DATE", favoriteMovies.releaseDate)
//            putExtra("DESCRIPTION", favoriteMovies.description)
//            putExtra("RATING", favoriteMovies.movie_rating.toString())
//        })
//    }
//
//    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
//        startActivity(Intent(this, TvSeriesInfoScreen::class.java).apply {
//            putExtra("TV_SERIES_NAME", favoriteTvSeries.title)
//            putExtra("TV_SERIES_PHOTO", favoriteTvSeries.photo)
//            putExtra("TV_SERIES_ID", favoriteTvSeries.id)
//            putExtra("TV_SERIES_RELEASE_DATE", favoriteTvSeries.releaseDate)
//            putExtra("TV_SERIES_DESCRIPTION", favoriteTvSeries.description)
//            putExtra("TV_SERIES_RATING", favoriteTvSeries.movie_rating.toString())
//        })
//    }
}