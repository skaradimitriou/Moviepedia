package com.stathis.moviepedia

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class PersonalizeAccountViewModel : ViewModel() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var imageUri: Uri
    private lateinit var storageRef:StorageReference
    private var downloadUrl : MutableLiveData<Bitmap> = MutableLiveData()

    fun saveUsername(string:String){
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("username").setValue(string)
    }

    fun uploadAndSaveBitmapUri(bitmap: Bitmap): MutableLiveData<Bitmap> {
        val baos = ByteArrayOutputStream()
        storageRef = FirebaseStorage.getInstance()
            .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)
        upload.addOnCompleteListener{uploadTask ->
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                    urlTask.result?.let{
                        imageUri = it
                        downloadUrl.postValue(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let{
                    //
                }
            }
        }
        return downloadUrl
    }
}