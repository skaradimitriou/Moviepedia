package com.stathis.moviepedia.ui.personalizeAccount

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stathis.moviepedia.R
import com.stathis.moviepedia.databinding.ActivityPersonalizeAccountBinding
import com.stathis.moviepedia.ui.dashboard.Dashboard
import kotlinx.android.synthetic.main.bottom_sheet_choose_option.view.*
import java.lang.Exception

class PersonalizeAccount : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private val IMAGE_PICK_CODE = 200
    private val PERMISSION_CODE = 201
    private lateinit var binding: ActivityPersonalizeAccountBinding
    private lateinit var viewModel: PersonalizeAccountViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PersonalizeAccountViewModel::class.java)
        binding = ActivityPersonalizeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding.personalisePhoto.setOnClickListener {
            showUploadPhotoOptions()
        }

        binding.getStartedBtn.setOnClickListener {
            val username: String = binding.username.text.toString()
            viewModel.saveUsername(username)
            startActivity(Intent(this, Dashboard::class.java))
        }
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
            viewModel.uploadAndSaveBitmapUri(imgBitmap)
            viewModel.retrieveUserImg()
        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // I have to save the url to the db
            val imageUri = data?.data
            if (imageUri != null) {
                viewModel.uploadAndSavePhoto(imageUri)
                Glide.with(this).load(viewModel.getUserPhoto()).into(binding.personalisePhoto)
//                binding.personalisePhoto.setImageURI(imageUri)
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

    override fun onBackPressed() {
        //
    }
}
