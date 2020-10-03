package com.stathis.moviepedia.ui.personalizeAccount

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.ViewModelProvider
import com.stathis.moviepedia.databinding.ActivityPersonalizeAccountBinding

class PersonalizeAccount : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var binding:ActivityPersonalizeAccountBinding
    private lateinit var viewModel : PersonalizeAccountViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PersonalizeAccountViewModel::class.java)
        binding = ActivityPersonalizeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding.personalisePhoto.setOnClickListener{
            takePictureIntent()
        }

        binding.getStartedBtn.setOnClickListener{
            val username:String = binding.username.text.toString()
            viewModel.saveUsername(username)
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
            viewModel.uploadAndSaveBitmapUri(imgBitmap)
        }
    }
}
