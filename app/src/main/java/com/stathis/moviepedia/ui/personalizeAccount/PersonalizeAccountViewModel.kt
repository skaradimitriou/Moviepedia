package com.stathis.moviepedia.ui.personalizeAccount

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class PersonalizeAccountViewModel : ViewModel() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var imageUri: Uri
    private var userImgPath: MutableLiveData<Bitmap> = MutableLiveData()
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private var downloadUrl: MutableLiveData<Bitmap> = MutableLiveData()
    private lateinit var imgUrl: String
    private var imageDownloadLink: MutableLiveData<String> = MutableLiveData()

    fun saveUsername(string: String) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("username").setValue(string)
    }

    fun uploadAndSaveBitmapUri(bitmap: Bitmap): MutableLiveData<Bitmap> {
        val baos = ByteArrayOutputStream()
        storageRef = FirebaseStorage.getInstance()
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