package com.stathis.moviepedia

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.stathis.moviepedia.databinding.ActivityPersonalizeAccountBinding
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_personalize_account.*
import java.io.ByteArrayOutputStream

class PersonalizeAccount : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var databaseReference:DatabaseReference
    private lateinit var binding:ActivityPersonalizeAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            saveUsername(username)
        }
    }

    private fun saveUsername(string:String){
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child(string).setValue(string)
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
                        binding.personalisePhoto.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let{
                    //
                }
            }
        }
    }

}
