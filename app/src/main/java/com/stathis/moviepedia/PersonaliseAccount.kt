package com.stathis.moviepedia

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_personalise_account.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import java.io.ByteArrayOutputStream

class PersonaliseAccount : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100
        private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalise_account)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        skipBtn.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
        }

        profileImg.setOnClickListener{
           takePictureIntent()
        }
    }

    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{pictureIntent ->
            pictureIntent.resolveActivity(packageManager!!)?.also{
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadAndSaveBitmapUri(imgBitmap)
        }
    }

    private fun uploadAndSaveBitmapUri(bitmap:Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)
        upload.addOnCompleteListener{uploadTask ->
            if(uploadTask.isSuccessful){
                    storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                        urlTask?.result?.let{
                            imageUri = it
                        }
                    }
                } else{
                uploadTask.exception?.let{
                    //
                }
            }
        }
    }
}
